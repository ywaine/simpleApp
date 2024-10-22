package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.collection.ArraySet;
import androidx.core.util.DebugUtils;
import androidx.core.util.LogWriter;
import androidx.core.view.OneShotPreDrawListener;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

final class FragmentManagerImpl extends FragmentManager implements LayoutInflater.Factory2 {
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
    static final Interpolator DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    final HashMap<String, Fragment> mActive = new HashMap<>();
    final ArrayList<Fragment> mAdded = new ArrayList<>();
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState = 0;
    boolean mDestroyed;
    Runnable mExecCommit = new Runnable() {
        public void run() {
            FragmentManagerImpl.this.execPendingActions();
        }
    };
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    private final CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> mLifecycleCallbacks = new CopyOnWriteArrayList<>();
    boolean mNeedMenuInvalidate;
    int mNextFragmentIndex = 0;
    private FragmentManagerViewModel mNonConfig;
    private final OnBackPressedCallback mOnBackPressedCallback = new OnBackPressedCallback(false) {
        public void handleOnBackPressed() {
            FragmentManagerImpl.this.handleOnBackPressed();
        }
    };
    private OnBackPressedDispatcher mOnBackPressedDispatcher;
    Fragment mParent;
    ArrayList<OpGenerator> mPendingActions;
    ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    SparseArray<Parcelable> mStateArray = null;
    Bundle mStateBundle = null;
    boolean mStateSaved;
    boolean mStopped;
    ArrayList<Fragment> mTmpAddedFragments;
    ArrayList<Boolean> mTmpIsPop;
    ArrayList<BackStackRecord> mTmpRecords;

    interface OpGenerator {
        boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2);
    }

    FragmentManagerImpl() {
    }

    private static final class FragmentLifecycleCallbacksHolder {
        final FragmentManager.FragmentLifecycleCallbacks mCallback;
        final boolean mRecursive;

        FragmentLifecycleCallbacksHolder(FragmentManager.FragmentLifecycleCallbacks callback, boolean recursive) {
            this.mCallback = callback;
            this.mRecursive = recursive;
        }
    }

    private void throwException(RuntimeException ex) {
        Log.e(TAG, ex.getMessage());
        Log.e(TAG, "Activity state:");
        PrintWriter pw = new PrintWriter(new LogWriter(TAG));
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            try {
                fragmentHostCallback.onDump("  ", (FileDescriptor) null, pw, new String[0]);
            } catch (Exception e) {
                Log.e(TAG, "Failed dumping state", e);
            }
        } else {
            try {
                dump("  ", (FileDescriptor) null, pw, new String[0]);
            } catch (Exception e2) {
                Log.e(TAG, "Failed dumping state", e2);
            }
        }
        throw ex;
    }

    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    public boolean executePendingTransactions() {
        boolean updates = execPendingActions();
        forcePostponedTransactions();
        return updates;
    }

    private void updateOnBackPressedCallbackEnabled() {
        ArrayList<OpGenerator> arrayList = this.mPendingActions;
        boolean z = true;
        if (arrayList == null || arrayList.isEmpty()) {
            OnBackPressedCallback onBackPressedCallback = this.mOnBackPressedCallback;
            if (getBackStackEntryCount() <= 0 || !isPrimaryNavigation(this.mParent)) {
                z = false;
            }
            onBackPressedCallback.setEnabled(z);
            return;
        }
        this.mOnBackPressedCallback.setEnabled(true);
    }

    /* access modifiers changed from: package-private */
    public boolean isPrimaryNavigation(Fragment parent) {
        if (parent == null) {
            return true;
        }
        FragmentManagerImpl parentFragmentManager = parent.mFragmentManager;
        if (parent != parentFragmentManager.getPrimaryNavigationFragment() || !isPrimaryNavigation(parentFragmentManager.mParent)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void handleOnBackPressed() {
        execPendingActions();
        if (this.mOnBackPressedCallback.isEnabled()) {
            popBackStackImmediate();
        } else {
            this.mOnBackPressedDispatcher.onBackPressed();
        }
    }

    public void popBackStack() {
        enqueueAction(new PopBackStackState((String) null, -1, 0), false);
    }

    public boolean popBackStackImmediate() {
        checkStateLoss();
        return popBackStackImmediate((String) null, -1, 0);
    }

    public void popBackStack(String name, int flags) {
        enqueueAction(new PopBackStackState(name, -1, flags), false);
    }

    public boolean popBackStackImmediate(String name, int flags) {
        checkStateLoss();
        return popBackStackImmediate(name, -1, flags);
    }

    public void popBackStack(int id, int flags) {
        if (id >= 0) {
            enqueueAction(new PopBackStackState((String) null, id, flags), false);
            return;
        }
        throw new IllegalArgumentException("Bad id: " + id);
    }

    public boolean popBackStackImmediate(int id, int flags) {
        checkStateLoss();
        execPendingActions();
        if (id >= 0) {
            return popBackStackImmediate((String) null, id, flags);
        }
        throw new IllegalArgumentException("Bad id: " + id);
    }

    private boolean popBackStackImmediate(String name, int id, int flags) {
        execPendingActions();
        ensureExecReady(true);
        Fragment fragment = this.mPrimaryNav;
        if (fragment != null && id < 0 && name == null && fragment.getChildFragmentManager().popBackStackImmediate()) {
            return true;
        }
        boolean executePop = popBackStackState(this.mTmpRecords, this.mTmpIsPop, name, id, flags);
        if (executePop) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                cleanupExec();
            }
        }
        updateOnBackPressedCallbackEnabled();
        doPendingDeferredStart();
        burpActive();
        return executePop;
    }

    public int getBackStackEntryCount() {
        ArrayList<BackStackRecord> arrayList = this.mBackStack;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public FragmentManager.BackStackEntry getBackStackEntryAt(int index) {
        return this.mBackStack.get(index);
    }

    public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList<>();
        }
        this.mBackStackChangeListeners.add(listener);
    }

    public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener listener) {
        ArrayList<FragmentManager.OnBackStackChangedListener> arrayList = this.mBackStackChangeListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
        }
    }

    public void putFragment(Bundle bundle, String key, Fragment fragment) {
        if (fragment.mFragmentManager != this) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        bundle.putString(key, fragment.mWho);
    }

    public Fragment getFragment(Bundle bundle, String key) {
        String who = bundle.getString(key);
        if (who == null) {
            return null;
        }
        Fragment f = this.mActive.get(who);
        if (f == null) {
            throwException(new IllegalStateException("Fragment no longer exists for key " + key + ": unique id " + who));
        }
        return f;
    }

    public List<Fragment> getFragments() {
        List<Fragment> list;
        if (this.mAdded.isEmpty()) {
            return Collections.emptyList();
        }
        synchronized (this.mAdded) {
            list = (List) this.mAdded.clone();
        }
        return list;
    }

    /* access modifiers changed from: package-private */
    public ViewModelStore getViewModelStore(Fragment f) {
        return this.mNonConfig.getViewModelStore(f);
    }

    /* access modifiers changed from: package-private */
    public FragmentManagerViewModel getChildNonConfig(Fragment f) {
        return this.mNonConfig.getChildNonConfig(f);
    }

    /* access modifiers changed from: package-private */
    public void addRetainedFragment(Fragment f) {
        if (isStateSaved()) {
            if (DEBUG) {
                Log.v(TAG, "Ignoring addRetainedFragment as the state is already saved");
            }
        } else if (this.mNonConfig.addRetainedFragment(f) && DEBUG) {
            Log.v(TAG, "Updating retained Fragments: Added " + f);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeRetainedFragment(Fragment f) {
        if (isStateSaved()) {
            if (DEBUG) {
                Log.v(TAG, "Ignoring removeRetainedFragment as the state is already saved");
            }
        } else if (this.mNonConfig.removeRetainedFragment(f) && DEBUG) {
            Log.v(TAG, "Updating retained Fragments: Removed " + f);
        }
    }

    /* access modifiers changed from: package-private */
    public List<Fragment> getActiveFragments() {
        return new ArrayList(this.mActive.values());
    }

    /* access modifiers changed from: package-private */
    public int getActiveFragmentCount() {
        return this.mActive.size();
    }

    public Fragment.SavedState saveFragmentInstanceState(Fragment fragment) {
        Bundle result;
        if (fragment.mFragmentManager != this) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        if (fragment.mState <= 0 || (result = saveFragmentBasicState(fragment)) == null) {
            return null;
        }
        return new Fragment.SavedState(result);
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        Fragment fragment = this.mParent;
        if (fragment != null) {
            DebugUtils.buildShortClassTag(fragment, sb);
        } else {
            DebugUtils.buildShortClassTag(this.mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        int N;
        int N2;
        int N3;
        int N4;
        String innerPrefix = prefix + "    ";
        if (!this.mActive.isEmpty()) {
            writer.print(prefix);
            writer.print("Active Fragments in ");
            writer.print(Integer.toHexString(System.identityHashCode(this)));
            writer.println(":");
            for (Fragment f : this.mActive.values()) {
                writer.print(prefix);
                writer.println(f);
                if (f != null) {
                    f.dump(innerPrefix, fd, writer, args);
                }
            }
        }
        int N5 = this.mAdded.size();
        if (N5 > 0) {
            writer.print(prefix);
            writer.println("Added Fragments:");
            for (int i = 0; i < N5; i++) {
                writer.print(prefix);
                writer.print("  #");
                writer.print(i);
                writer.print(": ");
                writer.println(this.mAdded.get(i).toString());
            }
        }
        ArrayList<Fragment> arrayList = this.mCreatedMenus;
        if (arrayList != null && (N4 = arrayList.size()) > 0) {
            writer.print(prefix);
            writer.println("Fragments Created Menus:");
            for (int i2 = 0; i2 < N4; i2++) {
                writer.print(prefix);
                writer.print("  #");
                writer.print(i2);
                writer.print(": ");
                writer.println(this.mCreatedMenus.get(i2).toString());
            }
        }
        ArrayList<BackStackRecord> arrayList2 = this.mBackStack;
        if (arrayList2 != null && (N3 = arrayList2.size()) > 0) {
            writer.print(prefix);
            writer.println("Back Stack:");
            for (int i3 = 0; i3 < N3; i3++) {
                BackStackRecord bs = this.mBackStack.get(i3);
                writer.print(prefix);
                writer.print("  #");
                writer.print(i3);
                writer.print(": ");
                writer.println(bs.toString());
                bs.dump(innerPrefix, writer);
            }
        }
        synchronized (this) {
            ArrayList<BackStackRecord> arrayList3 = this.mBackStackIndices;
            if (arrayList3 != null && (N2 = arrayList3.size()) > 0) {
                writer.print(prefix);
                writer.println("Back Stack Indices:");
                for (int i4 = 0; i4 < N2; i4++) {
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i4);
                    writer.print(": ");
                    writer.println(this.mBackStackIndices.get(i4));
                }
            }
            ArrayList<Integer> arrayList4 = this.mAvailBackStackIndices;
            if (arrayList4 != null && arrayList4.size() > 0) {
                writer.print(prefix);
                writer.print("mAvailBackStackIndices: ");
                writer.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
        }
        ArrayList<OpGenerator> arrayList5 = this.mPendingActions;
        if (arrayList5 != null && (N = arrayList5.size()) > 0) {
            writer.print(prefix);
            writer.println("Pending Actions:");
            for (int i5 = 0; i5 < N; i5++) {
                writer.print(prefix);
                writer.print("  #");
                writer.print(i5);
                writer.print(": ");
                writer.println(this.mPendingActions.get(i5));
            }
        }
        writer.print(prefix);
        writer.println("FragmentManager misc state:");
        writer.print(prefix);
        writer.print("  mHost=");
        writer.println(this.mHost);
        writer.print(prefix);
        writer.print("  mContainer=");
        writer.println(this.mContainer);
        if (this.mParent != null) {
            writer.print(prefix);
            writer.print("  mParent=");
            writer.println(this.mParent);
        }
        writer.print(prefix);
        writer.print("  mCurState=");
        writer.print(this.mCurState);
        writer.print(" mStateSaved=");
        writer.print(this.mStateSaved);
        writer.print(" mStopped=");
        writer.print(this.mStopped);
        writer.print(" mDestroyed=");
        writer.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            writer.print(prefix);
            writer.print("  mNeedMenuInvalidate=");
            writer.println(this.mNeedMenuInvalidate);
        }
    }

    static AnimationOrAnimator makeOpenCloseAnimation(float startScale, float endScale, float startAlpha, float endAlpha) {
        AnimationSet set = new AnimationSet(false);
        ScaleAnimation scale = new ScaleAnimation(startScale, endScale, startScale, endScale, 1, 0.5f, 1, 0.5f);
        scale.setInterpolator(DECELERATE_QUINT);
        scale.setDuration(220);
        set.addAnimation(scale);
        AlphaAnimation alpha = new AlphaAnimation(startAlpha, endAlpha);
        alpha.setInterpolator(DECELERATE_CUBIC);
        alpha.setDuration(220);
        set.addAnimation(alpha);
        return new AnimationOrAnimator((Animation) set);
    }

    static AnimationOrAnimator makeFadeAnimation(float start, float end) {
        AlphaAnimation anim = new AlphaAnimation(start, end);
        anim.setInterpolator(DECELERATE_CUBIC);
        anim.setDuration(220);
        return new AnimationOrAnimator((Animation) anim);
    }

    /* access modifiers changed from: package-private */
    public AnimationOrAnimator loadAnimation(Fragment fragment, int transit, boolean enter, int transitionStyle) {
        int styleIndex;
        int nextAnim = fragment.getNextAnim();
        fragment.setNextAnim(0);
        if (fragment.mContainer != null && fragment.mContainer.getLayoutTransition() != null) {
            return null;
        }
        Animation animation = fragment.onCreateAnimation(transit, enter, nextAnim);
        if (animation != null) {
            return new AnimationOrAnimator(animation);
        }
        Animator animator = fragment.onCreateAnimator(transit, enter, nextAnim);
        if (animator != null) {
            return new AnimationOrAnimator(animator);
        }
        if (nextAnim != 0) {
            boolean isAnim = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(nextAnim));
            boolean successfulLoad = false;
            if (isAnim) {
                try {
                    Animation animation2 = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                    if (animation2 != null) {
                        return new AnimationOrAnimator(animation2);
                    }
                    successfulLoad = true;
                } catch (Resources.NotFoundException e) {
                    throw e;
                } catch (RuntimeException e2) {
                }
            }
            if (!successfulLoad) {
                try {
                    Animator animator2 = AnimatorInflater.loadAnimator(this.mHost.getContext(), nextAnim);
                    if (animator2 != null) {
                        return new AnimationOrAnimator(animator2);
                    }
                } catch (RuntimeException e3) {
                    if (!isAnim) {
                        Animation animation3 = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                        if (animation3 != null) {
                            return new AnimationOrAnimator(animation3);
                        }
                    } else {
                        throw e3;
                    }
                }
            }
        }
        if (transit == 0 || (styleIndex = transitToStyleIndex(transit, enter)) < 0) {
            return null;
        }
        switch (styleIndex) {
            case 1:
                return makeOpenCloseAnimation(1.125f, 1.0f, 0.0f, 1.0f);
            case 2:
                return makeOpenCloseAnimation(1.0f, 0.975f, 1.0f, 0.0f);
            case 3:
                return makeOpenCloseAnimation(0.975f, 1.0f, 0.0f, 1.0f);
            case 4:
                return makeOpenCloseAnimation(1.0f, 1.075f, 1.0f, 0.0f);
            case 5:
                return makeFadeAnimation(0.0f, 1.0f);
            case 6:
                return makeFadeAnimation(1.0f, 0.0f);
            default:
                if (transitionStyle == 0 && this.mHost.onHasWindowAnimations()) {
                    int transitionStyle2 = this.mHost.onGetWindowAnimations();
                }
                return null;
        }
    }

    public void performPendingDeferredStart(Fragment f) {
        if (!f.mDeferStart) {
            return;
        }
        if (this.mExecutingActions) {
            this.mHavePendingDeferredStart = true;
            return;
        }
        f.mDeferStart = false;
        moveToState(f, this.mCurState, 0, 0, false);
    }

    /* access modifiers changed from: package-private */
    public boolean isStateAtLeast(int state) {
        return this.mCurState >= state;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x01fb, code lost:
        ensureInflatedFragmentView(r19);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x01fe, code lost:
        if (r1 <= 1) goto L_0x02f5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:0x0202, code lost:
        if (DEBUG == false) goto L_0x0218;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0204, code lost:
        android.util.Log.v(TAG, "moveto ACTIVITY_CREATED: " + r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x021a, code lost:
        if (r8.mFromLayout != false) goto L_0x02e0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x021c, code lost:
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x021f, code lost:
        if (r8.mContainerId == 0) goto L_0x0293;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x0224, code lost:
        if (r8.mContainerId != -1) goto L_0x0244;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x0226, code lost:
        throwException(new java.lang.IllegalArgumentException("Cannot create fragment " + r8 + " for a container view with no id"));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x0244, code lost:
        r2 = (android.view.ViewGroup) r7.mContainer.onFindViewById(r8.mContainerId);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x024e, code lost:
        if (r2 != null) goto L_0x0292;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:115:0x0252, code lost:
        if (r8.mRestored != false) goto L_0x0292;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:?, code lost:
        r0 = r19.getResources().getResourceName(r8.mContainerId);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x0260, code lost:
        r0 = androidx.core.p003os.EnvironmentCompat.MEDIA_UNKNOWN;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x0373, code lost:
        if (r0 >= 3) goto L_0x0393;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:169:0x0377, code lost:
        if (DEBUG == false) goto L_0x038d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:170:0x0379, code lost:
        android.util.Log.v(TAG, "movefrom STARTED: " + r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:171:0x038d, code lost:
        r19.performStop();
        dispatchOnFragmentStopped(r8, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:173:0x0394, code lost:
        if (r0 >= 2) goto L_0x043c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:0x0398, code lost:
        if (DEBUG == false) goto L_0x03ae;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:0x039a, code lost:
        android.util.Log.v(TAG, "movefrom ACTIVITY_CREATED: " + r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:178:0x03b0, code lost:
        if (r8.mView == null) goto L_0x03c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:180:0x03b8, code lost:
        if (r7.mHost.onShouldSaveFragmentState(r8) == false) goto L_0x03c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:182:0x03bc, code lost:
        if (r8.mSavedViewState != null) goto L_0x03c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:183:0x03be, code lost:
        saveFragmentViewState(r19);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:184:0x03c1, code lost:
        r19.performDestroyView();
        dispatchOnFragmentViewDestroyed(r8, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:185:0x03c9, code lost:
        if (r8.mView == null) goto L_0x0428;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:187:0x03cd, code lost:
        if (r8.mContainer == null) goto L_0x0428;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:188:0x03cf, code lost:
        r8.mContainer.endViewTransition(r8.mView);
        r8.mView.clearAnimation();
        r1 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:189:0x03e0, code lost:
        if (r19.getParentFragment() == null) goto L_0x03f0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:191:0x03e8, code lost:
        if (r19.getParentFragment().mRemoving != false) goto L_0x03eb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:192:0x03eb, code lost:
        r2 = r21;
        r4 = r22;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:194:0x03f3, code lost:
        if (r7.mCurState <= 0) goto L_0x0415;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:196:0x03f7, code lost:
        if (r7.mDestroyed != false) goto L_0x0415;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:198:0x03ff, code lost:
        if (r8.mView.getVisibility() != 0) goto L_0x0410;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:200:0x0405, code lost:
        if (r8.mPostponedAlpha < 0.0f) goto L_0x0410;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:201:0x0407, code lost:
        r1 = loadAnimation(r8, r21, false, r22);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:202:0x0410, code lost:
        r2 = r21;
        r4 = r22;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:203:0x0415, code lost:
        r2 = r21;
        r4 = r22;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:204:0x0419, code lost:
        r8.mPostponedAlpha = 0.0f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:205:0x041b, code lost:
        if (r1 == null) goto L_0x0420;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:206:0x041d, code lost:
        animateRemoveFragment(r8, r1, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:207:0x0420, code lost:
        r8.mContainer.removeView(r8.mView);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:208:0x0428, code lost:
        r2 = r21;
        r4 = r22;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:209:0x042c, code lost:
        r8.mContainer = null;
        r8.mView = null;
        r8.mViewLifecycleOwner = null;
        r8.mViewLifecycleOwnerLiveData.setValue(null);
        r8.mInnerView = null;
        r8.mInLayout = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:210:0x043c, code lost:
        r2 = r21;
        r4 = r22;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:212:0x0445, code lost:
        if (r0 >= 1) goto L_0x0522;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:214:0x0449, code lost:
        if (r7.mDestroyed == false) goto L_0x046d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:216:0x044f, code lost:
        if (r19.getAnimatingAway() == null) goto L_0x045c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:217:0x0451, code lost:
        r1 = r19.getAnimatingAway();
        r8.setAnimatingAway((android.view.View) null);
        r1.clearAnimation();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:219:0x0460, code lost:
        if (r19.getAnimator() == null) goto L_0x046d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:220:0x0462, code lost:
        r1 = r19.getAnimator();
        r8.setAnimator((android.animation.Animator) null);
        r1.cancel();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:222:0x0471, code lost:
        if (r19.getAnimatingAway() != null) goto L_0x0519;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:224:0x0477, code lost:
        if (r19.getAnimator() == null) goto L_0x047b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:226:0x047d, code lost:
        if (DEBUG == false) goto L_0x0493;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:227:0x047f, code lost:
        android.util.Log.v(TAG, "movefrom CREATED: " + r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:229:0x0495, code lost:
        if (r8.mRemoving == false) goto L_0x049f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:231:0x049b, code lost:
        if (r19.isInBackStack() != false) goto L_0x049f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:232:0x049d, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:233:0x049f, code lost:
        r1 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:234:0x04a0, code lost:
        if (r1 != false) goto L_0x04ae;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:236:0x04a8, code lost:
        if (r7.mNonConfig.shouldDestroy(r8) == false) goto L_0x04ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:237:0x04ab, code lost:
        r8.mState = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:238:0x04ae, code lost:
        r3 = r7.mHost;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:239:0x04b2, code lost:
        if ((r3 instanceof androidx.lifecycle.ViewModelStoreOwner) == false) goto L_0x04bb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:240:0x04b4, code lost:
        r3 = r7.mNonConfig.isCleared();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:242:0x04c1, code lost:
        if ((r3.getContext() instanceof android.app.Activity) == false) goto L_0x04d2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:243:0x04c3, code lost:
        r3 = !((android.app.Activity) r7.mHost.getContext()).isChangingConfigurations();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:244:0x04d2, code lost:
        r3 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:245:0x04d3, code lost:
        if (r1 != false) goto L_0x04d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:246:0x04d5, code lost:
        if (r3 == false) goto L_0x04dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:247:0x04d7, code lost:
        r7.mNonConfig.clearNonConfigState(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:248:0x04dc, code lost:
        r19.performDestroy();
        dispatchOnFragmentDestroyed(r8, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:249:0x04e3, code lost:
        r19.performDetach();
        dispatchOnFragmentDetached(r8, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:250:0x04e9, code lost:
        if (r23 != false) goto L_0x0522;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:251:0x04eb, code lost:
        if (r1 != false) goto L_0x0515;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:253:0x04f3, code lost:
        if (r7.mNonConfig.shouldDestroy(r8) == false) goto L_0x04f6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:254:0x04f6, code lost:
        r8.mHost = null;
        r8.mParentFragment = null;
        r8.mFragmentManager = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:255:0x04fe, code lost:
        if (r8.mTargetWho == null) goto L_0x0522;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:256:0x0500, code lost:
        r3 = r7.mActive.get(r8.mTargetWho);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:257:0x050a, code lost:
        if (r3 == null) goto L_0x0522;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:259:0x0510, code lost:
        if (r3.getRetainInstance() == false) goto L_0x0522;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:260:0x0512, code lost:
        r8.mTarget = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:261:0x0515, code lost:
        makeInactive(r19);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:262:0x0519, code lost:
        r8.setStateAfterAnimating(r0);
        r0 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x01f8, code lost:
        r1 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x01f9, code lost:
        if (r1 <= 0) goto L_0x01fe;
     */
    /* JADX WARNING: Removed duplicated region for block: B:266:0x0526  */
    /* JADX WARNING: Removed duplicated region for block: B:268:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void moveToState(androidx.fragment.app.Fragment r19, int r20, int r21, int r22, boolean r23) {
        /*
            r18 = this;
            r7 = r18
            r8 = r19
            boolean r0 = r8.mAdded
            r9 = 1
            if (r0 == 0) goto L_0x0011
            boolean r0 = r8.mDetached
            if (r0 == 0) goto L_0x000e
            goto L_0x0011
        L_0x000e:
            r0 = r20
            goto L_0x0016
        L_0x0011:
            r0 = r20
            if (r0 <= r9) goto L_0x0016
            r0 = 1
        L_0x0016:
            boolean r1 = r8.mRemoving
            if (r1 == 0) goto L_0x002c
            int r1 = r8.mState
            if (r0 <= r1) goto L_0x002c
            int r1 = r8.mState
            if (r1 != 0) goto L_0x002a
            boolean r1 = r19.isInBackStack()
            if (r1 == 0) goto L_0x002a
            r0 = 1
            goto L_0x002c
        L_0x002a:
            int r0 = r8.mState
        L_0x002c:
            boolean r1 = r8.mDeferStart
            r10 = 3
            r11 = 2
            if (r1 == 0) goto L_0x0039
            int r1 = r8.mState
            if (r1 >= r10) goto L_0x0039
            if (r0 <= r11) goto L_0x0039
            r0 = 2
        L_0x0039:
            androidx.lifecycle.Lifecycle$State r1 = r8.mMaxState
            androidx.lifecycle.Lifecycle$State r2 = androidx.lifecycle.Lifecycle.State.CREATED
            if (r1 != r2) goto L_0x0044
            int r0 = java.lang.Math.min(r0, r9)
            goto L_0x004e
        L_0x0044:
            androidx.lifecycle.Lifecycle$State r1 = r8.mMaxState
            int r1 = r1.ordinal()
            int r0 = java.lang.Math.min(r0, r1)
        L_0x004e:
            int r1 = r8.mState
            java.lang.String r12 = "FragmentManager"
            r13 = 0
            r14 = 0
            if (r1 > r0) goto L_0x0342
            boolean r1 = r8.mFromLayout
            if (r1 == 0) goto L_0x005f
            boolean r1 = r8.mInLayout
            if (r1 != 0) goto L_0x005f
            return
        L_0x005f:
            android.view.View r1 = r19.getAnimatingAway()
            if (r1 != 0) goto L_0x006b
            android.animation.Animator r1 = r19.getAnimator()
            if (r1 == 0) goto L_0x007f
        L_0x006b:
            r8.setAnimatingAway(r13)
            r8.setAnimator(r13)
            int r3 = r19.getStateAfterAnimating()
            r4 = 0
            r5 = 0
            r6 = 1
            r1 = r18
            r2 = r19
            r1.moveToState(r2, r3, r4, r5, r6)
        L_0x007f:
            int r1 = r8.mState
            switch(r1) {
                case 0: goto L_0x0086;
                case 1: goto L_0x01f8;
                case 2: goto L_0x02f6;
                case 3: goto L_0x0317;
                default: goto L_0x0084;
            }
        L_0x0084:
            goto L_0x033c
        L_0x0086:
            if (r0 <= 0) goto L_0x01f8
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x00a0
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "moveto CREATED: "
            r1.append(r2)
            r1.append(r8)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r12, r1)
        L_0x00a0:
            android.os.Bundle r1 = r8.mSavedFragmentState
            if (r1 == 0) goto L_0x00fd
            android.os.Bundle r1 = r8.mSavedFragmentState
            androidx.fragment.app.FragmentHostCallback r2 = r7.mHost
            android.content.Context r2 = r2.getContext()
            java.lang.ClassLoader r2 = r2.getClassLoader()
            r1.setClassLoader(r2)
            android.os.Bundle r1 = r8.mSavedFragmentState
            java.lang.String r2 = "android:view_state"
            android.util.SparseArray r1 = r1.getSparseParcelableArray(r2)
            r8.mSavedViewState = r1
            android.os.Bundle r1 = r8.mSavedFragmentState
            java.lang.String r2 = "android:target_state"
            androidx.fragment.app.Fragment r1 = r7.getFragment(r1, r2)
            if (r1 == 0) goto L_0x00ca
            java.lang.String r2 = r1.mWho
            goto L_0x00cb
        L_0x00ca:
            r2 = r13
        L_0x00cb:
            r8.mTargetWho = r2
            java.lang.String r2 = r8.mTargetWho
            if (r2 == 0) goto L_0x00db
            android.os.Bundle r2 = r8.mSavedFragmentState
            java.lang.String r3 = "android:target_req_state"
            int r2 = r2.getInt(r3, r14)
            r8.mTargetRequestCode = r2
        L_0x00db:
            java.lang.Boolean r2 = r8.mSavedUserVisibleHint
            if (r2 == 0) goto L_0x00ea
            java.lang.Boolean r2 = r8.mSavedUserVisibleHint
            boolean r2 = r2.booleanValue()
            r8.mUserVisibleHint = r2
            r8.mSavedUserVisibleHint = r13
            goto L_0x00f4
        L_0x00ea:
            android.os.Bundle r2 = r8.mSavedFragmentState
            java.lang.String r3 = "android:user_visible_hint"
            boolean r2 = r2.getBoolean(r3, r9)
            r8.mUserVisibleHint = r2
        L_0x00f4:
            boolean r2 = r8.mUserVisibleHint
            if (r2 != 0) goto L_0x00fd
            r8.mDeferStart = r9
            if (r0 <= r11) goto L_0x00fd
            r0 = 2
        L_0x00fd:
            androidx.fragment.app.FragmentHostCallback r1 = r7.mHost
            r8.mHost = r1
            androidx.fragment.app.Fragment r1 = r7.mParent
            r8.mParentFragment = r1
            androidx.fragment.app.Fragment r1 = r7.mParent
            if (r1 == 0) goto L_0x010c
            androidx.fragment.app.FragmentManagerImpl r1 = r1.mChildFragmentManager
            goto L_0x0110
        L_0x010c:
            androidx.fragment.app.FragmentHostCallback r1 = r7.mHost
            androidx.fragment.app.FragmentManagerImpl r1 = r1.mFragmentManager
        L_0x0110:
            r8.mFragmentManager = r1
            androidx.fragment.app.Fragment r1 = r8.mTarget
            java.lang.String r15 = " that does not belong to this FragmentManager!"
            java.lang.String r6 = " declared target fragment "
            java.lang.String r5 = "Fragment "
            if (r1 == 0) goto L_0x0171
            java.util.HashMap<java.lang.String, androidx.fragment.app.Fragment> r1 = r7.mActive
            androidx.fragment.app.Fragment r2 = r8.mTarget
            java.lang.String r2 = r2.mWho
            java.lang.Object r1 = r1.get(r2)
            androidx.fragment.app.Fragment r2 = r8.mTarget
            if (r1 != r2) goto L_0x014f
            androidx.fragment.app.Fragment r1 = r8.mTarget
            int r1 = r1.mState
            if (r1 >= r9) goto L_0x0144
            androidx.fragment.app.Fragment r2 = r8.mTarget
            r3 = 1
            r4 = 0
            r16 = 0
            r17 = 1
            r1 = r18
            r10 = r5
            r5 = r16
            r11 = r6
            r6 = r17
            r1.moveToState(r2, r3, r4, r5, r6)
            goto L_0x0146
        L_0x0144:
            r10 = r5
            r11 = r6
        L_0x0146:
            androidx.fragment.app.Fragment r1 = r8.mTarget
            java.lang.String r1 = r1.mWho
            r8.mTargetWho = r1
            r8.mTarget = r13
            goto L_0x0173
        L_0x014f:
            r10 = r5
            r11 = r6
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r10)
            r2.append(r8)
            r2.append(r11)
            androidx.fragment.app.Fragment r3 = r8.mTarget
            r2.append(r3)
            r2.append(r15)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L_0x0171:
            r10 = r5
            r11 = r6
        L_0x0173:
            java.lang.String r1 = r8.mTargetWho
            if (r1 == 0) goto L_0x01b9
            java.util.HashMap<java.lang.String, androidx.fragment.app.Fragment> r1 = r7.mActive
            java.lang.String r2 = r8.mTargetWho
            java.lang.Object r1 = r1.get(r2)
            r6 = r1
            androidx.fragment.app.Fragment r6 = (androidx.fragment.app.Fragment) r6
            if (r6 == 0) goto L_0x0199
            int r1 = r6.mState
            if (r1 >= r9) goto L_0x0196
            r3 = 1
            r4 = 0
            r5 = 0
            r10 = 1
            r1 = r18
            r2 = r6
            r17 = r6
            r6 = r10
            r1.moveToState(r2, r3, r4, r5, r6)
            goto L_0x01b9
        L_0x0196:
            r17 = r6
            goto L_0x01b9
        L_0x0199:
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r10)
            r2.append(r8)
            r2.append(r11)
            java.lang.String r3 = r8.mTargetWho
            r2.append(r3)
            r2.append(r15)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L_0x01b9:
            androidx.fragment.app.FragmentHostCallback r1 = r7.mHost
            android.content.Context r1 = r1.getContext()
            r7.dispatchOnFragmentPreAttached(r8, r1, r14)
            r19.performAttach()
            androidx.fragment.app.Fragment r1 = r8.mParentFragment
            if (r1 != 0) goto L_0x01cf
            androidx.fragment.app.FragmentHostCallback r1 = r7.mHost
            r1.onAttachFragment(r8)
            goto L_0x01d4
        L_0x01cf:
            androidx.fragment.app.Fragment r1 = r8.mParentFragment
            r1.onAttachFragment(r8)
        L_0x01d4:
            androidx.fragment.app.FragmentHostCallback r1 = r7.mHost
            android.content.Context r1 = r1.getContext()
            r7.dispatchOnFragmentAttached(r8, r1, r14)
            boolean r1 = r8.mIsCreated
            if (r1 != 0) goto L_0x01f1
            android.os.Bundle r1 = r8.mSavedFragmentState
            r7.dispatchOnFragmentPreCreated(r8, r1, r14)
            android.os.Bundle r1 = r8.mSavedFragmentState
            r8.performCreate(r1)
            android.os.Bundle r1 = r8.mSavedFragmentState
            r7.dispatchOnFragmentCreated(r8, r1, r14)
            goto L_0x01f8
        L_0x01f1:
            android.os.Bundle r1 = r8.mSavedFragmentState
            r8.restoreChildFragmentState(r1)
            r8.mState = r9
        L_0x01f8:
            r1 = r0
            if (r1 <= 0) goto L_0x01fe
            r18.ensureInflatedFragmentView(r19)
        L_0x01fe:
            if (r1 <= r9) goto L_0x02f5
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x0218
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "moveto ACTIVITY_CREATED: "
            r0.append(r2)
            r0.append(r8)
            java.lang.String r0 = r0.toString()
            android.util.Log.v(r12, r0)
        L_0x0218:
            boolean r0 = r8.mFromLayout
            if (r0 != 0) goto L_0x02e0
            r0 = 0
            int r2 = r8.mContainerId
            if (r2 == 0) goto L_0x0293
            int r2 = r8.mContainerId
            r3 = -1
            if (r2 != r3) goto L_0x0244
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Cannot create fragment "
            r3.append(r4)
            r3.append(r8)
            java.lang.String r4 = " for a container view with no id"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            r7.throwException(r2)
        L_0x0244:
            androidx.fragment.app.FragmentContainer r2 = r7.mContainer
            int r3 = r8.mContainerId
            android.view.View r2 = r2.onFindViewById(r3)
            android.view.ViewGroup r2 = (android.view.ViewGroup) r2
            if (r2 != 0) goto L_0x0292
            boolean r0 = r8.mRestored
            if (r0 != 0) goto L_0x0292
            android.content.res.Resources r0 = r19.getResources()     // Catch:{ NotFoundException -> 0x025f }
            int r3 = r8.mContainerId     // Catch:{ NotFoundException -> 0x025f }
            java.lang.String r0 = r0.getResourceName(r3)     // Catch:{ NotFoundException -> 0x025f }
            goto L_0x0263
        L_0x025f:
            r0 = move-exception
            java.lang.String r3 = "unknown"
            r0 = r3
        L_0x0263:
            java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "No view found for id 0x"
            r4.append(r5)
            int r5 = r8.mContainerId
            java.lang.String r5 = java.lang.Integer.toHexString(r5)
            r4.append(r5)
            java.lang.String r5 = " ("
            r4.append(r5)
            r4.append(r0)
            java.lang.String r5 = ") for fragment "
            r4.append(r5)
            r4.append(r8)
            java.lang.String r4 = r4.toString()
            r3.<init>(r4)
            r7.throwException(r3)
        L_0x0292:
            r0 = r2
        L_0x0293:
            r8.mContainer = r0
            android.os.Bundle r2 = r8.mSavedFragmentState
            android.view.LayoutInflater r2 = r8.performGetLayoutInflater(r2)
            android.os.Bundle r3 = r8.mSavedFragmentState
            r8.performCreateView(r2, r0, r3)
            android.view.View r2 = r8.mView
            if (r2 == 0) goto L_0x02de
            android.view.View r2 = r8.mView
            r8.mInnerView = r2
            android.view.View r2 = r8.mView
            r2.setSaveFromParentEnabled(r14)
            if (r0 == 0) goto L_0x02b4
            android.view.View r2 = r8.mView
            r0.addView(r2)
        L_0x02b4:
            boolean r2 = r8.mHidden
            if (r2 == 0) goto L_0x02bf
            android.view.View r2 = r8.mView
            r3 = 8
            r2.setVisibility(r3)
        L_0x02bf:
            android.view.View r2 = r8.mView
            android.os.Bundle r3 = r8.mSavedFragmentState
            r8.onViewCreated(r2, r3)
            android.view.View r2 = r8.mView
            android.os.Bundle r3 = r8.mSavedFragmentState
            r7.dispatchOnFragmentViewCreated(r8, r2, r3, r14)
            android.view.View r2 = r8.mView
            int r2 = r2.getVisibility()
            if (r2 != 0) goto L_0x02da
            android.view.ViewGroup r2 = r8.mContainer
            if (r2 == 0) goto L_0x02da
            goto L_0x02db
        L_0x02da:
            r9 = 0
        L_0x02db:
            r8.mIsNewlyAdded = r9
            goto L_0x02e0
        L_0x02de:
            r8.mInnerView = r13
        L_0x02e0:
            android.os.Bundle r0 = r8.mSavedFragmentState
            r8.performActivityCreated(r0)
            android.os.Bundle r0 = r8.mSavedFragmentState
            r7.dispatchOnFragmentActivityCreated(r8, r0, r14)
            android.view.View r0 = r8.mView
            if (r0 == 0) goto L_0x02f3
            android.os.Bundle r0 = r8.mSavedFragmentState
            r8.restoreViewState(r0)
        L_0x02f3:
            r8.mSavedFragmentState = r13
        L_0x02f5:
            r0 = r1
        L_0x02f6:
            r1 = 2
            if (r0 <= r1) goto L_0x0317
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0311
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "moveto STARTED: "
            r1.append(r2)
            r1.append(r8)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r12, r1)
        L_0x0311:
            r19.performStart()
            r7.dispatchOnFragmentStarted(r8, r14)
        L_0x0317:
            r1 = 3
            if (r0 <= r1) goto L_0x033c
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0332
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "moveto RESUMED: "
            r1.append(r2)
            r1.append(r8)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r12, r1)
        L_0x0332:
            r19.performResume()
            r7.dispatchOnFragmentResumed(r8, r14)
            r8.mSavedFragmentState = r13
            r8.mSavedViewState = r13
        L_0x033c:
            r2 = r21
            r4 = r22
            goto L_0x0522
        L_0x0342:
            int r1 = r8.mState
            if (r1 <= r0) goto L_0x051e
            int r1 = r8.mState
            switch(r1) {
                case 1: goto L_0x0441;
                case 2: goto L_0x0393;
                case 3: goto L_0x0372;
                case 4: goto L_0x0351;
                default: goto L_0x034b;
            }
        L_0x034b:
            r2 = r21
            r4 = r22
            goto L_0x0522
        L_0x0351:
            r1 = 4
            if (r0 >= r1) goto L_0x0372
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x036c
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "movefrom RESUMED: "
            r1.append(r2)
            r1.append(r8)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r12, r1)
        L_0x036c:
            r19.performPause()
            r7.dispatchOnFragmentPaused(r8, r14)
        L_0x0372:
            r1 = 3
            if (r0 >= r1) goto L_0x0393
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x038d
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "movefrom STARTED: "
            r1.append(r2)
            r1.append(r8)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r12, r1)
        L_0x038d:
            r19.performStop()
            r7.dispatchOnFragmentStopped(r8, r14)
        L_0x0393:
            r1 = 2
            if (r0 >= r1) goto L_0x043c
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x03ae
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "movefrom ACTIVITY_CREATED: "
            r1.append(r2)
            r1.append(r8)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r12, r1)
        L_0x03ae:
            android.view.View r1 = r8.mView
            if (r1 == 0) goto L_0x03c1
            androidx.fragment.app.FragmentHostCallback r1 = r7.mHost
            boolean r1 = r1.onShouldSaveFragmentState(r8)
            if (r1 == 0) goto L_0x03c1
            android.util.SparseArray<android.os.Parcelable> r1 = r8.mSavedViewState
            if (r1 != 0) goto L_0x03c1
            r18.saveFragmentViewState(r19)
        L_0x03c1:
            r19.performDestroyView()
            r7.dispatchOnFragmentViewDestroyed(r8, r14)
            android.view.View r1 = r8.mView
            if (r1 == 0) goto L_0x0428
            android.view.ViewGroup r1 = r8.mContainer
            if (r1 == 0) goto L_0x0428
            android.view.ViewGroup r1 = r8.mContainer
            android.view.View r2 = r8.mView
            r1.endViewTransition(r2)
            android.view.View r1 = r8.mView
            r1.clearAnimation()
            r1 = 0
            androidx.fragment.app.Fragment r2 = r19.getParentFragment()
            if (r2 == 0) goto L_0x03f0
            androidx.fragment.app.Fragment r2 = r19.getParentFragment()
            boolean r2 = r2.mRemoving
            if (r2 != 0) goto L_0x03eb
            goto L_0x03f0
        L_0x03eb:
            r2 = r21
            r4 = r22
            goto L_0x042c
        L_0x03f0:
            int r2 = r7.mCurState
            r3 = 0
            if (r2 <= 0) goto L_0x0415
            boolean r2 = r7.mDestroyed
            if (r2 != 0) goto L_0x0415
            android.view.View r2 = r8.mView
            int r2 = r2.getVisibility()
            if (r2 != 0) goto L_0x0410
            float r2 = r8.mPostponedAlpha
            int r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r2 < 0) goto L_0x0410
            r2 = r21
            r4 = r22
            androidx.fragment.app.FragmentManagerImpl$AnimationOrAnimator r1 = r7.loadAnimation(r8, r2, r14, r4)
            goto L_0x0419
        L_0x0410:
            r2 = r21
            r4 = r22
            goto L_0x0419
        L_0x0415:
            r2 = r21
            r4 = r22
        L_0x0419:
            r8.mPostponedAlpha = r3
            if (r1 == 0) goto L_0x0420
            r7.animateRemoveFragment(r8, r1, r0)
        L_0x0420:
            android.view.ViewGroup r3 = r8.mContainer
            android.view.View r5 = r8.mView
            r3.removeView(r5)
            goto L_0x042c
        L_0x0428:
            r2 = r21
            r4 = r22
        L_0x042c:
            r8.mContainer = r13
            r8.mView = r13
            r8.mViewLifecycleOwner = r13
            androidx.lifecycle.MutableLiveData<androidx.lifecycle.LifecycleOwner> r1 = r8.mViewLifecycleOwnerLiveData
            r1.setValue(r13)
            r8.mInnerView = r13
            r8.mInLayout = r14
            goto L_0x0445
        L_0x043c:
            r2 = r21
            r4 = r22
            goto L_0x0445
        L_0x0441:
            r2 = r21
            r4 = r22
        L_0x0445:
            if (r0 >= r9) goto L_0x0522
            boolean r1 = r7.mDestroyed
            if (r1 == 0) goto L_0x046d
            android.view.View r1 = r19.getAnimatingAway()
            if (r1 == 0) goto L_0x045c
            android.view.View r1 = r19.getAnimatingAway()
            r8.setAnimatingAway(r13)
            r1.clearAnimation()
            goto L_0x046d
        L_0x045c:
            android.animation.Animator r1 = r19.getAnimator()
            if (r1 == 0) goto L_0x046d
            android.animation.Animator r1 = r19.getAnimator()
            r8.setAnimator(r13)
            r1.cancel()
        L_0x046d:
            android.view.View r1 = r19.getAnimatingAway()
            if (r1 != 0) goto L_0x0519
            android.animation.Animator r1 = r19.getAnimator()
            if (r1 == 0) goto L_0x047b
            goto L_0x0519
        L_0x047b:
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0493
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "movefrom CREATED: "
            r1.append(r3)
            r1.append(r8)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r12, r1)
        L_0x0493:
            boolean r1 = r8.mRemoving
            if (r1 == 0) goto L_0x049f
            boolean r1 = r19.isInBackStack()
            if (r1 != 0) goto L_0x049f
            r1 = 1
            goto L_0x04a0
        L_0x049f:
            r1 = 0
        L_0x04a0:
            if (r1 != 0) goto L_0x04ae
            androidx.fragment.app.FragmentManagerViewModel r3 = r7.mNonConfig
            boolean r3 = r3.shouldDestroy(r8)
            if (r3 == 0) goto L_0x04ab
            goto L_0x04ae
        L_0x04ab:
            r8.mState = r14
            goto L_0x04e3
        L_0x04ae:
            androidx.fragment.app.FragmentHostCallback r3 = r7.mHost
            boolean r5 = r3 instanceof androidx.lifecycle.ViewModelStoreOwner
            if (r5 == 0) goto L_0x04bb
            androidx.fragment.app.FragmentManagerViewModel r3 = r7.mNonConfig
            boolean r3 = r3.isCleared()
            goto L_0x04d3
        L_0x04bb:
            android.content.Context r3 = r3.getContext()
            boolean r3 = r3 instanceof android.app.Activity
            if (r3 == 0) goto L_0x04d2
            androidx.fragment.app.FragmentHostCallback r3 = r7.mHost
            android.content.Context r3 = r3.getContext()
            android.app.Activity r3 = (android.app.Activity) r3
            boolean r5 = r3.isChangingConfigurations()
            r5 = r5 ^ r9
            r3 = r5
            goto L_0x04d3
        L_0x04d2:
            r3 = 1
        L_0x04d3:
            if (r1 != 0) goto L_0x04d7
            if (r3 == 0) goto L_0x04dc
        L_0x04d7:
            androidx.fragment.app.FragmentManagerViewModel r5 = r7.mNonConfig
            r5.clearNonConfigState(r8)
        L_0x04dc:
            r19.performDestroy()
            r7.dispatchOnFragmentDestroyed(r8, r14)
        L_0x04e3:
            r19.performDetach()
            r7.dispatchOnFragmentDetached(r8, r14)
            if (r23 != 0) goto L_0x0522
            if (r1 != 0) goto L_0x0515
            androidx.fragment.app.FragmentManagerViewModel r3 = r7.mNonConfig
            boolean r3 = r3.shouldDestroy(r8)
            if (r3 == 0) goto L_0x04f6
            goto L_0x0515
        L_0x04f6:
            r8.mHost = r13
            r8.mParentFragment = r13
            r8.mFragmentManager = r13
            java.lang.String r3 = r8.mTargetWho
            if (r3 == 0) goto L_0x0522
            java.util.HashMap<java.lang.String, androidx.fragment.app.Fragment> r3 = r7.mActive
            java.lang.String r5 = r8.mTargetWho
            java.lang.Object r3 = r3.get(r5)
            androidx.fragment.app.Fragment r3 = (androidx.fragment.app.Fragment) r3
            if (r3 == 0) goto L_0x0522
            boolean r5 = r3.getRetainInstance()
            if (r5 == 0) goto L_0x0522
            r8.mTarget = r3
            goto L_0x0522
        L_0x0515:
            r18.makeInactive(r19)
            goto L_0x0522
        L_0x0519:
            r8.setStateAfterAnimating(r0)
            r0 = 1
            goto L_0x0522
        L_0x051e:
            r2 = r21
            r4 = r22
        L_0x0522:
            int r1 = r8.mState
            if (r1 == r0) goto L_0x054e
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "moveToState: Fragment state for "
            r1.append(r3)
            r1.append(r8)
            java.lang.String r3 = " not updated inline; expected state "
            r1.append(r3)
            r1.append(r0)
            java.lang.String r3 = " found "
            r1.append(r3)
            int r3 = r8.mState
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            android.util.Log.w(r12, r1)
            r8.mState = r0
        L_0x054e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentManagerImpl.moveToState(androidx.fragment.app.Fragment, int, int, int, boolean):void");
    }

    private void animateRemoveFragment(final Fragment fragment, AnimationOrAnimator anim, int newState) {
        final View viewToAnimate = fragment.mView;
        final ViewGroup container = fragment.mContainer;
        container.startViewTransition(viewToAnimate);
        fragment.setStateAfterAnimating(newState);
        if (anim.animation != null) {
            Animation animation = new EndViewTransitionAnimation(anim.animation, container, viewToAnimate);
            fragment.setAnimatingAway(fragment.mView);
            animation.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    container.post(new Runnable() {
                        public void run() {
                            if (fragment.getAnimatingAway() != null) {
                                fragment.setAnimatingAway((View) null);
                                FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                            }
                        }
                    });
                }

                public void onAnimationRepeat(Animation animation) {
                }
            });
            fragment.mView.startAnimation(animation);
            return;
        }
        Animator animator = anim.animator;
        fragment.setAnimator(anim.animator);
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                container.endViewTransition(viewToAnimate);
                Animator animator = fragment.getAnimator();
                fragment.setAnimator((Animator) null);
                if (animator != null && container.indexOfChild(viewToAnimate) < 0) {
                    FragmentManagerImpl fragmentManagerImpl = FragmentManagerImpl.this;
                    Fragment fragment = fragment;
                    fragmentManagerImpl.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                }
            }
        });
        animator.setTarget(fragment.mView);
        animator.start();
    }

    /* access modifiers changed from: package-private */
    public void moveToState(Fragment f) {
        moveToState(f, this.mCurState, 0, 0, false);
    }

    /* access modifiers changed from: package-private */
    public void ensureInflatedFragmentView(Fragment f) {
        if (f.mFromLayout && !f.mPerformedCreateView) {
            f.performCreateView(f.performGetLayoutInflater(f.mSavedFragmentState), (ViewGroup) null, f.mSavedFragmentState);
            if (f.mView != null) {
                f.mInnerView = f.mView;
                f.mView.setSaveFromParentEnabled(false);
                if (f.mHidden) {
                    f.mView.setVisibility(8);
                }
                f.onViewCreated(f.mView, f.mSavedFragmentState);
                dispatchOnFragmentViewCreated(f, f.mView, f.mSavedFragmentState, false);
                return;
            }
            f.mInnerView = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void completeShowHideFragment(final Fragment fragment) {
        if (fragment.mView != null) {
            AnimationOrAnimator anim = loadAnimation(fragment, fragment.getNextTransition(), !fragment.mHidden, fragment.getNextTransitionStyle());
            if (anim == null || anim.animator == null) {
                if (anim != null) {
                    fragment.mView.startAnimation(anim.animation);
                    anim.animation.start();
                }
                fragment.mView.setVisibility((!fragment.mHidden || fragment.isHideReplaced()) ? 0 : 8);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            } else {
                anim.animator.setTarget(fragment.mView);
                if (!fragment.mHidden) {
                    fragment.mView.setVisibility(0);
                } else if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                } else {
                    final ViewGroup container = fragment.mContainer;
                    final View animatingView = fragment.mView;
                    container.startViewTransition(animatingView);
                    anim.animator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            container.endViewTransition(animatingView);
                            animation.removeListener(this);
                            if (fragment.mView != null && fragment.mHidden) {
                                fragment.mView.setVisibility(8);
                            }
                        }
                    });
                }
                anim.animator.start();
            }
        }
        if (fragment.mAdded && isMenuAvailable(fragment)) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x006a, code lost:
        r4 = r1.mView;
        r5 = r11.mContainer;
        r6 = r5.indexOfChild(r4);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void moveFragmentToExpectedState(androidx.fragment.app.Fragment r11) {
        /*
            r10 = this;
            if (r11 != 0) goto L_0x0003
            return
        L_0x0003:
            java.util.HashMap<java.lang.String, androidx.fragment.app.Fragment> r0 = r10.mActive
            java.lang.String r1 = r11.mWho
            boolean r0 = r0.containsKey(r1)
            if (r0 != 0) goto L_0x003a
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x0039
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Ignoring moving "
            r0.append(r1)
            r0.append(r11)
            java.lang.String r1 = " to state "
            r0.append(r1)
            int r1 = r10.mCurState
            r0.append(r1)
            java.lang.String r1 = "since it is not added to "
            r0.append(r1)
            r0.append(r10)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "FragmentManager"
            android.util.Log.v(r1, r0)
        L_0x0039:
            return
        L_0x003a:
            int r0 = r10.mCurState
            boolean r1 = r11.mRemoving
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0051
            boolean r1 = r11.isInBackStack()
            if (r1 == 0) goto L_0x004d
            int r0 = java.lang.Math.min(r0, r2)
            goto L_0x0051
        L_0x004d:
            int r0 = java.lang.Math.min(r0, r3)
        L_0x0051:
            int r7 = r11.getNextTransition()
            int r8 = r11.getNextTransitionStyle()
            r9 = 0
            r4 = r10
            r5 = r11
            r6 = r0
            r4.moveToState(r5, r6, r7, r8, r9)
            android.view.View r1 = r11.mView
            if (r1 == 0) goto L_0x00c2
            androidx.fragment.app.Fragment r1 = r10.findFragmentUnder(r11)
            if (r1 == 0) goto L_0x0082
            android.view.View r4 = r1.mView
            android.view.ViewGroup r5 = r11.mContainer
            int r6 = r5.indexOfChild(r4)
            android.view.View r7 = r11.mView
            int r7 = r5.indexOfChild(r7)
            if (r7 >= r6) goto L_0x0082
            r5.removeViewAt(r7)
            android.view.View r8 = r11.mView
            r5.addView(r8, r6)
        L_0x0082:
            boolean r4 = r11.mIsNewlyAdded
            if (r4 == 0) goto L_0x00c2
            android.view.ViewGroup r4 = r11.mContainer
            if (r4 == 0) goto L_0x00c2
            float r4 = r11.mPostponedAlpha
            r5 = 0
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 <= 0) goto L_0x0098
            android.view.View r4 = r11.mView
            float r6 = r11.mPostponedAlpha
            r4.setAlpha(r6)
        L_0x0098:
            r11.mPostponedAlpha = r5
            r11.mIsNewlyAdded = r3
            int r3 = r11.getNextTransition()
            int r4 = r11.getNextTransitionStyle()
            androidx.fragment.app.FragmentManagerImpl$AnimationOrAnimator r2 = r10.loadAnimation(r11, r3, r2, r4)
            if (r2 == 0) goto L_0x00c2
            android.view.animation.Animation r3 = r2.animation
            if (r3 == 0) goto L_0x00b6
            android.view.View r3 = r11.mView
            android.view.animation.Animation r4 = r2.animation
            r3.startAnimation(r4)
            goto L_0x00c2
        L_0x00b6:
            android.animation.Animator r3 = r2.animator
            android.view.View r4 = r11.mView
            r3.setTarget(r4)
            android.animation.Animator r3 = r2.animator
            r3.start()
        L_0x00c2:
            boolean r1 = r11.mHiddenChanged
            if (r1 == 0) goto L_0x00c9
            r10.completeShowHideFragment(r11)
        L_0x00c9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentManagerImpl.moveFragmentToExpectedState(androidx.fragment.app.Fragment):void");
    }

    /* access modifiers changed from: package-private */
    public void moveToState(int newState, boolean always) {
        FragmentHostCallback fragmentHostCallback;
        if (this.mHost == null && newState != 0) {
            throw new IllegalStateException("No activity");
        } else if (always || newState != this.mCurState) {
            this.mCurState = newState;
            int numAdded = this.mAdded.size();
            for (int i = 0; i < numAdded; i++) {
                moveFragmentToExpectedState(this.mAdded.get(i));
            }
            for (Fragment f : this.mActive.values()) {
                if (f != null && ((f.mRemoving || f.mDetached) && !f.mIsNewlyAdded)) {
                    moveFragmentToExpectedState(f);
                }
            }
            startPendingDeferredFragments();
            if (this.mNeedMenuInvalidate && (fragmentHostCallback = this.mHost) != null && this.mCurState == 4) {
                fragmentHostCallback.onSupportInvalidateOptionsMenu();
                this.mNeedMenuInvalidate = false;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void startPendingDeferredFragments() {
        for (Fragment f : this.mActive.values()) {
            if (f != null) {
                performPendingDeferredStart(f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void makeActive(Fragment f) {
        if (this.mActive.get(f.mWho) == null) {
            this.mActive.put(f.mWho, f);
            if (f.mRetainInstanceChangedWhileDetached) {
                if (f.mRetainInstance) {
                    addRetainedFragment(f);
                } else {
                    removeRetainedFragment(f);
                }
                f.mRetainInstanceChangedWhileDetached = false;
            }
            if (DEBUG) {
                Log.v(TAG, "Added fragment to active set " + f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void makeInactive(Fragment f) {
        if (this.mActive.get(f.mWho) != null) {
            if (DEBUG) {
                Log.v(TAG, "Removed fragment from active set " + f);
            }
            for (Fragment fragment : this.mActive.values()) {
                if (fragment != null && f.mWho.equals(fragment.mTargetWho)) {
                    fragment.mTarget = f;
                    fragment.mTargetWho = null;
                }
            }
            this.mActive.put(f.mWho, (Object) null);
            removeRetainedFragment(f);
            if (f.mTargetWho != null) {
                f.mTarget = this.mActive.get(f.mTargetWho);
            }
            f.initState();
        }
    }

    public void addFragment(Fragment fragment, boolean moveToStateNow) {
        if (DEBUG) {
            Log.v(TAG, "add: " + fragment);
        }
        makeActive(fragment);
        if (fragment.mDetached) {
            return;
        }
        if (!this.mAdded.contains(fragment)) {
            synchronized (this.mAdded) {
                this.mAdded.add(fragment);
            }
            fragment.mAdded = true;
            fragment.mRemoving = false;
            if (fragment.mView == null) {
                fragment.mHiddenChanged = false;
            }
            if (isMenuAvailable(fragment)) {
                this.mNeedMenuInvalidate = true;
            }
            if (moveToStateNow) {
                moveToState(fragment);
                return;
            }
            return;
        }
        throw new IllegalStateException("Fragment already added: " + fragment);
    }

    public void removeFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
        }
        boolean inactive = !fragment.isInBackStack();
        if (!fragment.mDetached || inactive) {
            synchronized (this.mAdded) {
                this.mAdded.remove(fragment);
            }
            if (isMenuAvailable(fragment)) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
        }
    }

    public void hideFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "hide: " + fragment);
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged = true ^ fragment.mHiddenChanged;
        }
    }

    public void showFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "show: " + fragment);
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged = !fragment.mHiddenChanged;
        }
    }

    public void detachFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "detach: " + fragment);
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (DEBUG) {
                    Log.v(TAG, "remove from detach: " + fragment);
                }
                synchronized (this.mAdded) {
                    this.mAdded.remove(fragment);
                }
                if (isMenuAvailable(fragment)) {
                    this.mNeedMenuInvalidate = true;
                }
                fragment.mAdded = false;
            }
        }
    }

    public void attachFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "attach: " + fragment);
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (fragment.mAdded) {
                return;
            }
            if (!this.mAdded.contains(fragment)) {
                if (DEBUG) {
                    Log.v(TAG, "add from attach: " + fragment);
                }
                synchronized (this.mAdded) {
                    this.mAdded.add(fragment);
                }
                fragment.mAdded = true;
                if (isMenuAvailable(fragment)) {
                    this.mNeedMenuInvalidate = true;
                    return;
                }
                return;
            }
            throw new IllegalStateException("Fragment already added: " + fragment);
        }
    }

    public Fragment findFragmentById(int id) {
        for (int i = this.mAdded.size() - 1; i >= 0; i--) {
            Fragment f = this.mAdded.get(i);
            if (f != null && f.mFragmentId == id) {
                return f;
            }
        }
        for (Fragment f2 : this.mActive.values()) {
            if (f2 != null && f2.mFragmentId == id) {
                return f2;
            }
        }
        return null;
    }

    public Fragment findFragmentByTag(String tag) {
        if (tag != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; i--) {
                Fragment f = this.mAdded.get(i);
                if (f != null && tag.equals(f.mTag)) {
                    return f;
                }
            }
        }
        if (tag == null) {
            return null;
        }
        for (Fragment f2 : this.mActive.values()) {
            if (f2 != null && tag.equals(f2.mTag)) {
                return f2;
            }
        }
        return null;
    }

    public Fragment findFragmentByWho(String who) {
        for (Fragment f : this.mActive.values()) {
            if (f != null) {
                Fragment findFragmentByWho = f.findFragmentByWho(who);
                Fragment f2 = findFragmentByWho;
                if (findFragmentByWho != null) {
                    return f2;
                }
            }
        }
        return null;
    }

    private void checkStateLoss() {
        if (isStateSaved()) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
    }

    public boolean isStateSaved() {
        return this.mStateSaved || this.mStopped;
    }

    public void enqueueAction(OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss) {
            checkStateLoss();
        }
        synchronized (this) {
            if (!this.mDestroyed) {
                if (this.mHost != null) {
                    if (this.mPendingActions == null) {
                        this.mPendingActions = new ArrayList<>();
                    }
                    this.mPendingActions.add(action);
                    scheduleCommit();
                    return;
                }
            }
            if (!allowStateLoss) {
                throw new IllegalStateException("Activity has been destroyed");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void scheduleCommit() {
        synchronized (this) {
            ArrayList<StartEnterTransitionListener> arrayList = this.mPostponedTransactions;
            boolean pendingReady = false;
            boolean postponeReady = arrayList != null && !arrayList.isEmpty();
            ArrayList<OpGenerator> arrayList2 = this.mPendingActions;
            if (arrayList2 != null && arrayList2.size() == 1) {
                pendingReady = true;
            }
            if (postponeReady || pendingReady) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
                updateOnBackPressedCallbackEnabled();
            }
        }
    }

    public int allocBackStackIndex(BackStackRecord bse) {
        synchronized (this) {
            ArrayList<Integer> arrayList = this.mAvailBackStackIndices;
            if (arrayList != null) {
                if (arrayList.size() > 0) {
                    ArrayList<Integer> arrayList2 = this.mAvailBackStackIndices;
                    int index = arrayList2.remove(arrayList2.size() - 1).intValue();
                    if (DEBUG) {
                        Log.v(TAG, "Adding back stack index " + index + " with " + bse);
                    }
                    this.mBackStackIndices.set(index, bse);
                    return index;
                }
            }
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<>();
            }
            int index2 = this.mBackStackIndices.size();
            if (DEBUG) {
                Log.v(TAG, "Setting back stack index " + index2 + " to " + bse);
            }
            this.mBackStackIndices.add(bse);
            return index2;
        }
    }

    public void setBackStackIndex(int index, BackStackRecord bse) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<>();
            }
            int N = this.mBackStackIndices.size();
            if (index < N) {
                if (DEBUG) {
                    Log.v(TAG, "Setting back stack index " + index + " to " + bse);
                }
                this.mBackStackIndices.set(index, bse);
            } else {
                while (N < index) {
                    this.mBackStackIndices.add((Object) null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList<>();
                    }
                    if (DEBUG) {
                        Log.v(TAG, "Adding available back stack index " + N);
                    }
                    this.mAvailBackStackIndices.add(Integer.valueOf(N));
                    N++;
                }
                if (DEBUG) {
                    Log.v(TAG, "Adding back stack index " + index + " with " + bse);
                }
                this.mBackStackIndices.add(bse);
            }
        }
    }

    public void freeBackStackIndex(int index) {
        synchronized (this) {
            this.mBackStackIndices.set(index, (Object) null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList<>();
            }
            if (DEBUG) {
                Log.v(TAG, "Freeing back stack index " + index);
            }
            this.mAvailBackStackIndices.add(Integer.valueOf(index));
        }
    }

    private void ensureExecReady(boolean allowStateLoss) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        } else if (this.mHost == null) {
            throw new IllegalStateException("Fragment host has been destroyed");
        } else if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
            if (!allowStateLoss) {
                checkStateLoss();
            }
            if (this.mTmpRecords == null) {
                this.mTmpRecords = new ArrayList<>();
                this.mTmpIsPop = new ArrayList<>();
            }
            this.mExecutingActions = true;
            try {
                executePostponedTransaction((ArrayList<BackStackRecord>) null, (ArrayList<Boolean>) null);
            } finally {
                this.mExecutingActions = false;
            }
        } else {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
    }

    public void execSingleAction(OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss || (this.mHost != null && !this.mDestroyed)) {
            ensureExecReady(allowStateLoss);
            if (action.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
                this.mExecutingActions = true;
                try {
                    removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                } finally {
                    cleanupExec();
                }
            }
            updateOnBackPressedCallbackEnabled();
            doPendingDeferredStart();
            burpActive();
        }
    }

    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    /* JADX INFO: finally extract failed */
    public boolean execPendingActions() {
        ensureExecReady(true);
        boolean didSomething = false;
        while (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                cleanupExec();
                didSomething = true;
            } catch (Throwable th) {
                cleanupExec();
                throw th;
            }
        }
        updateOnBackPressedCallbackEnabled();
        doPendingDeferredStart();
        burpActive();
        return didSomething;
    }

    private void executePostponedTransaction(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
        int index;
        ArrayList<StartEnterTransitionListener> arrayList = this.mPostponedTransactions;
        int numPostponed = arrayList == null ? 0 : arrayList.size();
        int i = 0;
        while (i < numPostponed) {
            StartEnterTransitionListener listener = this.mPostponedTransactions.get(i);
            if (records != null && !listener.mIsBack && (index = records.indexOf(listener.mRecord)) != -1 && isRecordPop.get(index).booleanValue()) {
                this.mPostponedTransactions.remove(i);
                i--;
                numPostponed--;
                listener.cancelTransaction();
            } else if (listener.isReady() != 0 || (records != null && listener.mRecord.interactsWith(records, 0, records.size()))) {
                this.mPostponedTransactions.remove(i);
                i--;
                numPostponed--;
                if (records != null && !listener.mIsBack) {
                    int indexOf = records.indexOf(listener.mRecord);
                    int index2 = indexOf;
                    if (indexOf != -1 && isRecordPop.get(index2).booleanValue()) {
                        listener.cancelTransaction();
                    }
                }
                listener.completeTransaction();
            }
            i++;
        }
    }

    private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
        if (records != null && !records.isEmpty()) {
            if (isRecordPop == null || records.size() != isRecordPop.size()) {
                throw new IllegalStateException("Internal error with the back stack records");
            }
            executePostponedTransaction(records, isRecordPop);
            int numRecords = records.size();
            int startIndex = 0;
            int recordNum = 0;
            while (recordNum < numRecords) {
                if (!records.get(recordNum).mReorderingAllowed) {
                    if (startIndex != recordNum) {
                        executeOpsTogether(records, isRecordPop, startIndex, recordNum);
                    }
                    int reorderingEnd = recordNum + 1;
                    if (isRecordPop.get(recordNum).booleanValue()) {
                        while (reorderingEnd < numRecords && isRecordPop.get(reorderingEnd).booleanValue() && !records.get(reorderingEnd).mReorderingAllowed) {
                            reorderingEnd++;
                        }
                    }
                    executeOpsTogether(records, isRecordPop, recordNum, reorderingEnd);
                    startIndex = reorderingEnd;
                    recordNum = reorderingEnd - 1;
                }
                recordNum++;
            }
            if (startIndex != numRecords) {
                executeOpsTogether(records, isRecordPop, startIndex, numRecords);
            }
        }
    }

    private void executeOpsTogether(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        ArrayList<BackStackRecord> arrayList = records;
        ArrayList<Boolean> arrayList2 = isRecordPop;
        int i = startIndex;
        int i2 = endIndex;
        boolean allowReordering = arrayList.get(i).mReorderingAllowed;
        ArrayList<Fragment> arrayList3 = this.mTmpAddedFragments;
        if (arrayList3 == null) {
            this.mTmpAddedFragments = new ArrayList<>();
        } else {
            arrayList3.clear();
        }
        this.mTmpAddedFragments.addAll(this.mAdded);
        int recordNum = startIndex;
        boolean addToBackStack = false;
        Fragment oldPrimaryNav = getPrimaryNavigationFragment();
        while (true) {
            boolean z = true;
            if (recordNum >= i2) {
                break;
            }
            BackStackRecord record = arrayList.get(recordNum);
            if (!arrayList2.get(recordNum).booleanValue()) {
                oldPrimaryNav = record.expandOps(this.mTmpAddedFragments, oldPrimaryNav);
            } else {
                oldPrimaryNav = record.trackAddedFragmentsInPop(this.mTmpAddedFragments, oldPrimaryNav);
            }
            if (!addToBackStack && !record.mAddToBackStack) {
                z = false;
            }
            addToBackStack = z;
            recordNum++;
        }
        this.mTmpAddedFragments.clear();
        if (!allowReordering) {
            FragmentTransition.startTransitions(this, records, isRecordPop, startIndex, endIndex, false);
        }
        executeOps(records, isRecordPop, startIndex, endIndex);
        int postponeIndex = endIndex;
        if (allowReordering) {
            ArraySet<Fragment> addedFragments = new ArraySet<>();
            addAddedFragments(addedFragments);
            postponeIndex = postponePostponableTransactions(records, isRecordPop, startIndex, endIndex, addedFragments);
            makeRemovedFragmentsInvisible(addedFragments);
        }
        if (postponeIndex != i && allowReordering) {
            FragmentTransition.startTransitions(this, records, isRecordPop, startIndex, postponeIndex, true);
            moveToState(this.mCurState, true);
        }
        for (int recordNum2 = startIndex; recordNum2 < i2; recordNum2++) {
            BackStackRecord record2 = arrayList.get(recordNum2);
            if (arrayList2.get(recordNum2).booleanValue() && record2.mIndex >= 0) {
                freeBackStackIndex(record2.mIndex);
                record2.mIndex = -1;
            }
            record2.runOnCommitRunnables();
        }
        if (addToBackStack) {
            reportBackStackChanged();
        }
    }

    private void makeRemovedFragmentsInvisible(ArraySet<Fragment> fragments) {
        int numAdded = fragments.size();
        for (int i = 0; i < numAdded; i++) {
            Fragment fragment = fragments.valueAt(i);
            if (!fragment.mAdded) {
                View view = fragment.requireView();
                fragment.mPostponedAlpha = view.getAlpha();
                view.setAlpha(0.0f);
            }
        }
    }

    private int postponePostponableTransactions(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex, ArraySet<Fragment> added) {
        int postponeIndex = endIndex;
        for (int i = endIndex - 1; i >= startIndex; i--) {
            BackStackRecord record = records.get(i);
            boolean isPop = isRecordPop.get(i).booleanValue();
            if (record.isPostponed() && !record.interactsWith(records, i + 1, endIndex)) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList<>();
                }
                StartEnterTransitionListener listener = new StartEnterTransitionListener(record, isPop);
                this.mPostponedTransactions.add(listener);
                record.setOnStartPostponedListener(listener);
                if (isPop) {
                    record.executeOps();
                } else {
                    record.executePopOps(false);
                }
                postponeIndex--;
                if (i != postponeIndex) {
                    records.remove(i);
                    records.add(postponeIndex, record);
                }
                addAddedFragments(added);
            }
        }
        return postponeIndex;
    }

    /* access modifiers changed from: package-private */
    public void completeExecute(BackStackRecord record, boolean isPop, boolean runTransitions, boolean moveToState) {
        if (isPop) {
            record.executePopOps(moveToState);
        } else {
            record.executeOps();
        }
        ArrayList<BackStackRecord> records = new ArrayList<>(1);
        ArrayList arrayList = new ArrayList(1);
        records.add(record);
        arrayList.add(Boolean.valueOf(isPop));
        if (runTransitions) {
            FragmentTransition.startTransitions(this, records, arrayList, 0, 1, true);
        }
        if (moveToState) {
            moveToState(this.mCurState, true);
        }
        for (Fragment fragment : this.mActive.values()) {
            if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && record.interactsWith(fragment.mContainerId)) {
                if (fragment.mPostponedAlpha > 0.0f) {
                    fragment.mView.setAlpha(fragment.mPostponedAlpha);
                }
                if (moveToState) {
                    fragment.mPostponedAlpha = 0.0f;
                } else {
                    fragment.mPostponedAlpha = -1.0f;
                    fragment.mIsNewlyAdded = false;
                }
            }
        }
    }

    private Fragment findFragmentUnder(Fragment f) {
        ViewGroup container = f.mContainer;
        View view = f.mView;
        if (container == null || view == null) {
            return null;
        }
        for (int i = this.mAdded.indexOf(f) - 1; i >= 0; i--) {
            Fragment underFragment = this.mAdded.get(i);
            if (underFragment.mContainer == container && underFragment.mView != null) {
                return underFragment;
            }
        }
        return null;
    }

    private static void executeOps(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            BackStackRecord record = records.get(i);
            boolean moveToState = true;
            if (isRecordPop.get(i).booleanValue()) {
                record.bumpBackStackNesting(-1);
                if (i != endIndex - 1) {
                    moveToState = false;
                }
                record.executePopOps(moveToState);
            } else {
                record.bumpBackStackNesting(1);
                record.executeOps();
            }
        }
    }

    private void addAddedFragments(ArraySet<Fragment> added) {
        int i = this.mCurState;
        if (i >= 1) {
            int state = Math.min(i, 3);
            int numAdded = this.mAdded.size();
            for (int i2 = 0; i2 < numAdded; i2++) {
                Fragment fragment = this.mAdded.get(i2);
                if (fragment.mState < state) {
                    moveToState(fragment, state, fragment.getNextAnim(), fragment.getNextTransition(), false);
                    if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded) {
                        added.add(fragment);
                    }
                }
            }
        }
    }

    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                this.mPostponedTransactions.remove(0).completeTransaction();
            }
        }
    }

    private void endAnimatingAwayFragments() {
        for (Fragment fragment : this.mActive.values()) {
            if (fragment != null) {
                if (fragment.getAnimatingAway() != null) {
                    int stateAfterAnimating = fragment.getStateAfterAnimating();
                    View animatingAway = fragment.getAnimatingAway();
                    Animation animation = animatingAway.getAnimation();
                    if (animation != null) {
                        animation.cancel();
                        animatingAway.clearAnimation();
                    }
                    fragment.setAnimatingAway((View) null);
                    moveToState(fragment, stateAfterAnimating, 0, 0, false);
                } else if (fragment.getAnimator() != null) {
                    fragment.getAnimator().end();
                }
            }
        }
    }

    private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> records, ArrayList<Boolean> isPop) {
        boolean didSomething = false;
        synchronized (this) {
            ArrayList<OpGenerator> arrayList = this.mPendingActions;
            if (arrayList != null) {
                if (arrayList.size() != 0) {
                    int numActions = this.mPendingActions.size();
                    for (int i = 0; i < numActions; i++) {
                        didSomething |= this.mPendingActions.get(i).generateOps(records, isPop);
                    }
                    this.mPendingActions.clear();
                    this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                    return didSomething;
                }
            }
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            this.mHavePendingDeferredStart = false;
            startPendingDeferredFragments();
        }
    }

    /* access modifiers changed from: package-private */
    public void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); i++) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void addBackStackState(BackStackRecord state) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList<>();
        }
        this.mBackStack.add(state);
    }

    /* access modifiers changed from: package-private */
    public boolean popBackStackState(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, String name, int id, int flags) {
        ArrayList<BackStackRecord> arrayList = this.mBackStack;
        if (arrayList == null) {
            return false;
        }
        if (name == null && id < 0 && (flags & 1) == 0) {
            int last = arrayList.size() - 1;
            if (last < 0) {
                return false;
            }
            records.add(this.mBackStack.remove(last));
            isRecordPop.add(true);
        } else {
            int index = -1;
            if (name != null || id >= 0) {
                int index2 = arrayList.size() - 1;
                while (index >= 0) {
                    BackStackRecord bss = this.mBackStack.get(index);
                    if ((name != null && name.equals(bss.getName())) || (id >= 0 && id == bss.mIndex)) {
                        break;
                    }
                    index2 = index - 1;
                }
                if (index < 0) {
                    return false;
                }
                if ((flags & 1) != 0) {
                    index--;
                    while (index >= 0) {
                        BackStackRecord bss2 = this.mBackStack.get(index);
                        if ((name == null || !name.equals(bss2.getName())) && (id < 0 || id != bss2.mIndex)) {
                            break;
                        }
                        index--;
                    }
                }
            }
            if (index == this.mBackStack.size() - 1) {
                return false;
            }
            for (int i = this.mBackStack.size() - 1; i > index; i--) {
                records.add(this.mBackStack.remove(i));
                isRecordPop.add(true);
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    @Deprecated
    public FragmentManagerNonConfig retainNonConfig() {
        if (this.mHost instanceof ViewModelStoreOwner) {
            throwException(new IllegalStateException("You cannot use retainNonConfig when your FragmentHostCallback implements ViewModelStoreOwner."));
        }
        return this.mNonConfig.getSnapshot();
    }

    /* access modifiers changed from: package-private */
    public void saveFragmentViewState(Fragment f) {
        if (f.mInnerView != null) {
            SparseArray<Parcelable> sparseArray = this.mStateArray;
            if (sparseArray == null) {
                this.mStateArray = new SparseArray<>();
            } else {
                sparseArray.clear();
            }
            f.mInnerView.saveHierarchyState(this.mStateArray);
            if (this.mStateArray.size() > 0) {
                f.mSavedViewState = this.mStateArray;
                this.mStateArray = null;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Bundle saveFragmentBasicState(Fragment f) {
        Bundle result = null;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        f.performSaveInstanceState(this.mStateBundle);
        dispatchOnFragmentSaveInstanceState(f, this.mStateBundle, false);
        if (!this.mStateBundle.isEmpty()) {
            result = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (f.mView != null) {
            saveFragmentViewState(f);
        }
        if (f.mSavedViewState != null) {
            if (result == null) {
                result = new Bundle();
            }
            result.putSparseParcelableArray(VIEW_STATE_TAG, f.mSavedViewState);
        }
        if (!f.mUserVisibleHint) {
            if (result == null) {
                result = new Bundle();
            }
            result.putBoolean(USER_VISIBLE_HINT_TAG, f.mUserVisibleHint);
        }
        return result;
    }

    /* access modifiers changed from: package-private */
    public Parcelable saveAllState() {
        int size;
        forcePostponedTransactions();
        endAnimatingAwayFragments();
        execPendingActions();
        this.mStateSaved = true;
        if (this.mActive.isEmpty()) {
            return null;
        }
        ArrayList<FragmentState> active = new ArrayList<>(this.mActive.size());
        boolean haveFragments = false;
        for (Fragment f : this.mActive.values()) {
            if (f != null) {
                if (f.mFragmentManager != this) {
                    throwException(new IllegalStateException("Failure saving state: active " + f + " was removed from the FragmentManager"));
                }
                haveFragments = true;
                FragmentState fs = new FragmentState(f);
                active.add(fs);
                if (f.mState <= 0 || fs.mSavedFragmentState != null) {
                    fs.mSavedFragmentState = f.mSavedFragmentState;
                } else {
                    fs.mSavedFragmentState = saveFragmentBasicState(f);
                    if (f.mTargetWho != null) {
                        Fragment target = this.mActive.get(f.mTargetWho);
                        if (target == null) {
                            throwException(new IllegalStateException("Failure saving state: " + f + " has target not in fragment manager: " + f.mTargetWho));
                        }
                        if (fs.mSavedFragmentState == null) {
                            fs.mSavedFragmentState = new Bundle();
                        }
                        putFragment(fs.mSavedFragmentState, TARGET_STATE_TAG, target);
                        if (f.mTargetRequestCode != 0) {
                            fs.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, f.mTargetRequestCode);
                        }
                    }
                }
                if (DEBUG) {
                    Log.v(TAG, "Saved state of " + f + ": " + fs.mSavedFragmentState);
                }
            }
        }
        if (!haveFragments) {
            if (DEBUG) {
                Log.v(TAG, "saveAllState: no fragments!");
            }
            return null;
        }
        ArrayList<String> added = null;
        BackStackState[] backStack = null;
        int size2 = this.mAdded.size();
        if (size2 > 0) {
            added = new ArrayList<>(size2);
            Iterator<Fragment> it = this.mAdded.iterator();
            while (it.hasNext()) {
                Fragment f2 = it.next();
                added.add(f2.mWho);
                if (f2.mFragmentManager != this) {
                    throwException(new IllegalStateException("Failure saving state: active " + f2 + " was removed from the FragmentManager"));
                }
                if (DEBUG) {
                    Log.v(TAG, "saveAllState: adding fragment (" + f2.mWho + "): " + f2);
                }
            }
        }
        ArrayList<BackStackRecord> arrayList = this.mBackStack;
        if (arrayList != null && (size = arrayList.size()) > 0) {
            backStack = new BackStackState[size];
            for (int i = 0; i < size; i++) {
                backStack[i] = new BackStackState(this.mBackStack.get(i));
                if (DEBUG) {
                    Log.v(TAG, "saveAllState: adding back stack #" + i + ": " + this.mBackStack.get(i));
                }
            }
        }
        FragmentManagerState fms = new FragmentManagerState();
        fms.mActive = active;
        fms.mAdded = added;
        fms.mBackStack = backStack;
        Fragment fragment = this.mPrimaryNav;
        if (fragment != null) {
            fms.mPrimaryNavActiveWho = fragment.mWho;
        }
        fms.mNextFragmentIndex = this.mNextFragmentIndex;
        return fms;
    }

    /* access modifiers changed from: package-private */
    public void restoreAllState(Parcelable state, FragmentManagerNonConfig nonConfig) {
        if (this.mHost instanceof ViewModelStoreOwner) {
            throwException(new IllegalStateException("You must use restoreSaveState when your FragmentHostCallback implements ViewModelStoreOwner"));
        }
        this.mNonConfig.restoreFromSnapshot(nonConfig);
        restoreSaveState(state);
    }

    /* access modifiers changed from: package-private */
    public void restoreSaveState(Parcelable state) {
        FragmentState fs;
        if (state != null) {
            FragmentManagerState fms = (FragmentManagerState) state;
            if (fms.mActive != null) {
                for (Fragment f : this.mNonConfig.getRetainedFragments()) {
                    if (DEBUG) {
                        Log.v(TAG, "restoreSaveState: re-attaching retained " + f);
                    }
                    Iterator<FragmentState> it = fms.mActive.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            fs = null;
                            break;
                        }
                        FragmentState fragmentState = it.next();
                        if (fragmentState.mWho.equals(f.mWho)) {
                            fs = fragmentState;
                            break;
                        }
                    }
                    if (fs == null) {
                        if (DEBUG) {
                            Log.v(TAG, "Discarding retained Fragment " + f + " that was not found in the set of active Fragments " + fms.mActive);
                        }
                        Fragment fragment = f;
                        moveToState(fragment, 1, 0, 0, false);
                        f.mRemoving = true;
                        moveToState(fragment, 0, 0, 0, false);
                    } else {
                        fs.mInstance = f;
                        f.mSavedViewState = null;
                        f.mBackStackNesting = 0;
                        f.mInLayout = false;
                        f.mAdded = false;
                        f.mTargetWho = f.mTarget != null ? f.mTarget.mWho : null;
                        f.mTarget = null;
                        if (fs.mSavedFragmentState != null) {
                            fs.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                            f.mSavedViewState = fs.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
                            f.mSavedFragmentState = fs.mSavedFragmentState;
                        }
                    }
                }
                this.mActive.clear();
                Iterator<FragmentState> it2 = fms.mActive.iterator();
                while (it2.hasNext()) {
                    FragmentState fs2 = it2.next();
                    if (fs2 != null) {
                        Fragment f2 = fs2.instantiate(this.mHost.getContext().getClassLoader(), getFragmentFactory());
                        f2.mFragmentManager = this;
                        if (DEBUG) {
                            Log.v(TAG, "restoreSaveState: active (" + f2.mWho + "): " + f2);
                        }
                        this.mActive.put(f2.mWho, f2);
                        fs2.mInstance = null;
                    }
                }
                this.mAdded.clear();
                if (fms.mAdded != null) {
                    Iterator<String> it3 = fms.mAdded.iterator();
                    while (it3.hasNext()) {
                        String who = it3.next();
                        Fragment f3 = this.mActive.get(who);
                        if (f3 == null) {
                            throwException(new IllegalStateException("No instantiated fragment for (" + who + ")"));
                        }
                        f3.mAdded = true;
                        if (DEBUG) {
                            Log.v(TAG, "restoreSaveState: added (" + who + "): " + f3);
                        }
                        if (!this.mAdded.contains(f3)) {
                            synchronized (this.mAdded) {
                                this.mAdded.add(f3);
                            }
                        } else {
                            throw new IllegalStateException("Already added " + f3);
                        }
                    }
                }
                if (fms.mBackStack != null) {
                    this.mBackStack = new ArrayList<>(fms.mBackStack.length);
                    for (int i = 0; i < fms.mBackStack.length; i++) {
                        BackStackRecord bse = fms.mBackStack[i].instantiate(this);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: back stack #" + i + " (index " + bse.mIndex + "): " + bse);
                            PrintWriter pw = new PrintWriter(new LogWriter(TAG));
                            bse.dump("  ", pw, false);
                            pw.close();
                        }
                        this.mBackStack.add(bse);
                        if (bse.mIndex >= 0) {
                            setBackStackIndex(bse.mIndex, bse);
                        }
                    }
                } else {
                    this.mBackStack = null;
                }
                if (fms.mPrimaryNavActiveWho != null) {
                    Fragment fragment2 = this.mActive.get(fms.mPrimaryNavActiveWho);
                    this.mPrimaryNav = fragment2;
                    dispatchParentPrimaryNavigationFragmentChanged(fragment2);
                }
                this.mNextFragmentIndex = fms.mNextFragmentIndex;
            }
        }
    }

    private void burpActive() {
        this.mActive.values().removeAll(Collections.singleton((Object) null));
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v11, resolved type: androidx.activity.OnBackPressedDispatcherOwner} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: androidx.fragment.app.Fragment} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: androidx.fragment.app.Fragment} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: androidx.fragment.app.Fragment} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void attachController(androidx.fragment.app.FragmentHostCallback r5, androidx.fragment.app.FragmentContainer r6, androidx.fragment.app.Fragment r7) {
        /*
            r4 = this;
            androidx.fragment.app.FragmentHostCallback r0 = r4.mHost
            if (r0 != 0) goto L_0x004c
            r4.mHost = r5
            r4.mContainer = r6
            r4.mParent = r7
            if (r7 == 0) goto L_0x000f
            r4.updateOnBackPressedCallbackEnabled()
        L_0x000f:
            boolean r0 = r5 instanceof androidx.activity.OnBackPressedDispatcherOwner
            if (r0 == 0) goto L_0x0026
            r0 = r5
            androidx.activity.OnBackPressedDispatcherOwner r0 = (androidx.activity.OnBackPressedDispatcherOwner) r0
            androidx.activity.OnBackPressedDispatcher r1 = r0.getOnBackPressedDispatcher()
            r4.mOnBackPressedDispatcher = r1
            if (r7 == 0) goto L_0x0020
            r2 = r7
            goto L_0x0021
        L_0x0020:
            r2 = r0
        L_0x0021:
            androidx.activity.OnBackPressedCallback r3 = r4.mOnBackPressedCallback
            r1.addCallback(r2, r3)
        L_0x0026:
            if (r7 == 0) goto L_0x0031
            androidx.fragment.app.FragmentManagerImpl r0 = r7.mFragmentManager
            androidx.fragment.app.FragmentManagerViewModel r0 = r0.getChildNonConfig(r7)
            r4.mNonConfig = r0
            goto L_0x004b
        L_0x0031:
            boolean r0 = r5 instanceof androidx.lifecycle.ViewModelStoreOwner
            if (r0 == 0) goto L_0x0043
            r0 = r5
            androidx.lifecycle.ViewModelStoreOwner r0 = (androidx.lifecycle.ViewModelStoreOwner) r0
            androidx.lifecycle.ViewModelStore r0 = r0.getViewModelStore()
            androidx.fragment.app.FragmentManagerViewModel r1 = androidx.fragment.app.FragmentManagerViewModel.getInstance(r0)
            r4.mNonConfig = r1
            goto L_0x004b
        L_0x0043:
            androidx.fragment.app.FragmentManagerViewModel r0 = new androidx.fragment.app.FragmentManagerViewModel
            r1 = 0
            r0.<init>(r1)
            r4.mNonConfig = r0
        L_0x004b:
            return
        L_0x004c:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "Already attached"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentManagerImpl.attachController(androidx.fragment.app.FragmentHostCallback, androidx.fragment.app.FragmentContainer, androidx.fragment.app.Fragment):void");
    }

    public void noteStateNotSaved() {
        this.mStateSaved = false;
        this.mStopped = false;
        int addedCount = this.mAdded.size();
        for (int i = 0; i < addedCount; i++) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.noteStateNotSaved();
            }
        }
    }

    public void dispatchCreate() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(1);
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(2);
    }

    public void dispatchStart() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(3);
    }

    public void dispatchResume() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(4);
    }

    public void dispatchPause() {
        dispatchStateChange(3);
    }

    public void dispatchStop() {
        this.mStopped = true;
        dispatchStateChange(2);
    }

    public void dispatchDestroyView() {
        dispatchStateChange(1);
    }

    public void dispatchDestroy() {
        this.mDestroyed = true;
        execPendingActions();
        dispatchStateChange(0);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
        if (this.mOnBackPressedDispatcher != null) {
            this.mOnBackPressedCallback.remove();
            this.mOnBackPressedDispatcher = null;
        }
    }

    /* JADX INFO: finally extract failed */
    private void dispatchStateChange(int nextState) {
        try {
            this.mExecutingActions = true;
            moveToState(nextState, false);
            this.mExecutingActions = false;
            execPendingActions();
        } catch (Throwable th) {
            this.mExecutingActions = false;
            throw th;
        }
    }

    public void dispatchMultiWindowModeChanged(boolean isInMultiWindowMode) {
        for (int i = this.mAdded.size() - 1; i >= 0; i--) {
            Fragment f = this.mAdded.get(i);
            if (f != null) {
                f.performMultiWindowModeChanged(isInMultiWindowMode);
            }
        }
    }

    public void dispatchPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        for (int i = this.mAdded.size() - 1; i >= 0; i--) {
            Fragment f = this.mAdded.get(i);
            if (f != null) {
                f.performPictureInPictureModeChanged(isInPictureInPictureMode);
            }
        }
    }

    public void dispatchConfigurationChanged(Configuration newConfig) {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = this.mAdded.get(i);
            if (f != null) {
                f.performConfigurationChanged(newConfig);
            }
        }
    }

    public void dispatchLowMemory() {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = this.mAdded.get(i);
            if (f != null) {
                f.performLowMemory();
            }
        }
    }

    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean show = false;
        ArrayList<Fragment> newMenus = null;
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = this.mAdded.get(i);
            if (f != null && f.performCreateOptionsMenu(menu, inflater)) {
                show = true;
                if (newMenus == null) {
                    newMenus = new ArrayList<>();
                }
                newMenus.add(f);
            }
        }
        if (this.mCreatedMenus != null) {
            for (int i2 = 0; i2 < this.mCreatedMenus.size(); i2++) {
                Fragment f2 = this.mCreatedMenus.get(i2);
                if (newMenus == null || !newMenus.contains(f2)) {
                    f2.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = newMenus;
        return show;
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean show = false;
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = this.mAdded.get(i);
            if (f != null && f.performPrepareOptionsMenu(menu)) {
                show = true;
            }
        }
        return show;
    }

    public boolean dispatchOptionsItemSelected(MenuItem item) {
        if (this.mCurState < 1) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = this.mAdded.get(i);
            if (f != null && f.performOptionsItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean dispatchContextItemSelected(MenuItem item) {
        if (this.mCurState < 1) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = this.mAdded.get(i);
            if (f != null && f.performContextItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        if (this.mCurState >= 1) {
            for (int i = 0; i < this.mAdded.size(); i++) {
                Fragment f = this.mAdded.get(i);
                if (f != null) {
                    f.performOptionsMenuClosed(menu);
                }
            }
        }
    }

    public void setPrimaryNavigationFragment(Fragment f) {
        if (f == null || (this.mActive.get(f.mWho) == f && (f.mHost == null || f.getFragmentManager() == this))) {
            Fragment previousPrimaryNav = this.mPrimaryNav;
            this.mPrimaryNav = f;
            dispatchParentPrimaryNavigationFragmentChanged(previousPrimaryNav);
            dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
            return;
        }
        throw new IllegalArgumentException("Fragment " + f + " is not an active fragment of FragmentManager " + this);
    }

    private void dispatchParentPrimaryNavigationFragmentChanged(Fragment f) {
        if (f != null && this.mActive.get(f.mWho) == f) {
            f.performPrimaryNavigationFragmentChanged();
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchPrimaryNavigationFragmentChanged() {
        updateOnBackPressedCallbackEnabled();
        dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
    }

    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }

    public void setMaxLifecycle(Fragment f, Lifecycle.State state) {
        if (this.mActive.get(f.mWho) == f && (f.mHost == null || f.getFragmentManager() == this)) {
            f.mMaxState = state;
            return;
        }
        throw new IllegalArgumentException("Fragment " + f + " is not an active fragment of FragmentManager " + this);
    }

    public FragmentFactory getFragmentFactory() {
        if (super.getFragmentFactory() == DEFAULT_FACTORY) {
            Fragment fragment = this.mParent;
            if (fragment != null) {
                return fragment.mFragmentManager.getFragmentFactory();
            }
            setFragmentFactory(new FragmentFactory() {
                public Fragment instantiate(ClassLoader classLoader, String className) {
                    return FragmentManagerImpl.this.mHost.instantiate(FragmentManagerImpl.this.mHost.getContext(), className, (Bundle) null);
                }
            });
        }
        return super.getFragmentFactory();
    }

    public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks cb, boolean recursive) {
        this.mLifecycleCallbacks.add(new FragmentLifecycleCallbacksHolder(cb, recursive));
    }

    public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks cb) {
        synchronized (this.mLifecycleCallbacks) {
            int i = 0;
            int N = this.mLifecycleCallbacks.size();
            while (true) {
                if (i >= N) {
                    break;
                } else if (this.mLifecycleCallbacks.get(i).mCallback == cb) {
                    this.mLifecycleCallbacks.remove(i);
                    break;
                } else {
                    i++;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentPreAttached(Fragment f, Context context, boolean onlyRecursive) {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            FragmentManager parentManager = fragment.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentPreAttached(f, context, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder holder = it.next();
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentPreAttached(this, f, context);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentAttached(Fragment f, Context context, boolean onlyRecursive) {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            FragmentManager parentManager = fragment.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentAttached(f, context, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder holder = it.next();
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentAttached(this, f, context);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentPreCreated(Fragment f, Bundle savedInstanceState, boolean onlyRecursive) {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            FragmentManager parentManager = fragment.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentPreCreated(f, savedInstanceState, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder holder = it.next();
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentPreCreated(this, f, savedInstanceState);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentCreated(Fragment f, Bundle savedInstanceState, boolean onlyRecursive) {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            FragmentManager parentManager = fragment.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentCreated(f, savedInstanceState, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder holder = it.next();
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentCreated(this, f, savedInstanceState);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentActivityCreated(Fragment f, Bundle savedInstanceState, boolean onlyRecursive) {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            FragmentManager parentManager = fragment.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentActivityCreated(f, savedInstanceState, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder holder = it.next();
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentActivityCreated(this, f, savedInstanceState);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentViewCreated(Fragment f, View v, Bundle savedInstanceState, boolean onlyRecursive) {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            FragmentManager parentManager = fragment.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentViewCreated(f, v, savedInstanceState, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder holder = it.next();
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentViewCreated(this, f, v, savedInstanceState);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentStarted(Fragment f, boolean onlyRecursive) {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            FragmentManager parentManager = fragment.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentStarted(f, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder holder = it.next();
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentStarted(this, f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentResumed(Fragment f, boolean onlyRecursive) {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            FragmentManager parentManager = fragment.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentResumed(f, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder holder = it.next();
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentResumed(this, f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentPaused(Fragment f, boolean onlyRecursive) {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            FragmentManager parentManager = fragment.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentPaused(f, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder holder = it.next();
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentPaused(this, f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentStopped(Fragment f, boolean onlyRecursive) {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            FragmentManager parentManager = fragment.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentStopped(f, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder holder = it.next();
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentStopped(this, f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentSaveInstanceState(Fragment f, Bundle outState, boolean onlyRecursive) {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            FragmentManager parentManager = fragment.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentSaveInstanceState(f, outState, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder holder = it.next();
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentSaveInstanceState(this, f, outState);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentViewDestroyed(Fragment f, boolean onlyRecursive) {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            FragmentManager parentManager = fragment.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentViewDestroyed(f, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder holder = it.next();
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentViewDestroyed(this, f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentDestroyed(Fragment f, boolean onlyRecursive) {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            FragmentManager parentManager = fragment.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentDestroyed(f, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder holder = it.next();
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentDestroyed(this, f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentDetached(Fragment f, boolean onlyRecursive) {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            FragmentManager parentManager = fragment.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentDetached(f, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder holder = it.next();
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentDetached(this, f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean checkForMenus() {
        boolean hasMenu = false;
        for (Fragment f : this.mActive.values()) {
            if (f != null) {
                hasMenu = isMenuAvailable(f);
                continue;
            }
            if (hasMenu) {
                return true;
            }
        }
        return false;
    }

    private boolean isMenuAvailable(Fragment f) {
        return (f.mHasMenu && f.mMenuVisible) || f.mChildFragmentManager.checkForMenus();
    }

    public static int reverseTransit(int transit) {
        switch (transit) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
                return 8194;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE:
                return FragmentTransaction.TRANSIT_FRAGMENT_FADE;
            case 8194:
                return FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
            default:
                return 0;
        }
    }

    public static int transitToStyleIndex(int transit, boolean enter) {
        switch (transit) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
                return enter ? 1 : 2;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE:
                return enter ? 5 : 6;
            case 8194:
                return enter ? 3 : 4;
            default:
                return -1;
        }
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        String fname;
        Fragment fragment;
        AttributeSet attributeSet = attrs;
        Fragment fragment2 = null;
        if (!"fragment".equals(name)) {
            return null;
        }
        String fname2 = attributeSet.getAttributeValue((String) null, "class");
        TypedArray a = context.obtainStyledAttributes(attributeSet, FragmentTag.Fragment);
        int i = 0;
        if (fname2 == null) {
            fname = a.getString(0);
        } else {
            fname = fname2;
        }
        int id = a.getResourceId(1, -1);
        String tag = a.getString(2);
        a.recycle();
        if (fname == null || !FragmentFactory.isFragmentClass(context.getClassLoader(), fname)) {
            return null;
        }
        if (parent != null) {
            i = parent.getId();
        }
        int containerId = i;
        if (containerId == -1 && id == -1 && tag == null) {
            throw new IllegalArgumentException(attrs.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + fname);
        }
        if (id != -1) {
            fragment2 = findFragmentById(id);
        }
        if (fragment2 == null && tag != null) {
            fragment2 = findFragmentByTag(tag);
        }
        if (fragment2 == null && containerId != -1) {
            fragment2 = findFragmentById(containerId);
        }
        if (DEBUG) {
            Log.v(TAG, "onCreateView: id=0x" + Integer.toHexString(id) + " fname=" + fname + " existing=" + fragment2);
        }
        if (fragment2 == null) {
            Fragment fragment3 = getFragmentFactory().instantiate(context.getClassLoader(), fname);
            fragment3.mFromLayout = true;
            fragment3.mFragmentId = id != 0 ? id : containerId;
            fragment3.mContainerId = containerId;
            fragment3.mTag = tag;
            fragment3.mInLayout = true;
            fragment3.mFragmentManager = this;
            fragment3.mHost = this.mHost;
            fragment3.onInflate(this.mHost.getContext(), attributeSet, fragment3.mSavedFragmentState);
            addFragment(fragment3, true);
            fragment = fragment3;
        } else if (!fragment2.mInLayout) {
            fragment2.mInLayout = true;
            fragment2.mHost = this.mHost;
            fragment2.onInflate(this.mHost.getContext(), attributeSet, fragment2.mSavedFragmentState);
            fragment = fragment2;
        } else {
            throw new IllegalArgumentException(attrs.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(id) + ", tag " + tag + ", or parent id 0x" + Integer.toHexString(containerId) + " with another fragment for " + fname);
        }
        if (this.mCurState >= 1 || !fragment.mFromLayout) {
            moveToState(fragment);
        } else {
            moveToState(fragment, 1, 0, 0, false);
        }
        if (fragment.mView != null) {
            if (id != 0) {
                fragment.mView.setId(id);
            }
            if (fragment.mView.getTag() == null) {
                fragment.mView.setTag(tag);
            }
            return fragment.mView;
        }
        throw new IllegalStateException("Fragment " + fname + " did not create a view.");
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return onCreateView((View) null, name, context, attrs);
    }

    /* access modifiers changed from: package-private */
    public LayoutInflater.Factory2 getLayoutInflaterFactory() {
        return this;
    }

    static class FragmentTag {
        public static final int[] Fragment = {16842755, 16842960, 16842961};
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;

        private FragmentTag() {
        }
    }

    private class PopBackStackState implements OpGenerator {
        final int mFlags;
        final int mId;
        final String mName;

        PopBackStackState(String name, int id, int flags) {
            this.mName = name;
            this.mId = id;
            this.mFlags = flags;
        }

        public boolean generateOps(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
            if (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null && FragmentManagerImpl.this.mPrimaryNav.getChildFragmentManager().popBackStackImmediate()) {
                return false;
            }
            return FragmentManagerImpl.this.popBackStackState(records, isRecordPop, this.mName, this.mId, this.mFlags);
        }
    }

    static class StartEnterTransitionListener implements Fragment.OnStartEnterTransitionListener {
        final boolean mIsBack;
        private int mNumPostponed;
        final BackStackRecord mRecord;

        StartEnterTransitionListener(BackStackRecord record, boolean isBack) {
            this.mIsBack = isBack;
            this.mRecord = record;
        }

        public void onStartEnterTransition() {
            int i = this.mNumPostponed - 1;
            this.mNumPostponed = i;
            if (i == 0) {
                this.mRecord.mManager.scheduleCommit();
            }
        }

        public void startListening() {
            this.mNumPostponed++;
        }

        public boolean isReady() {
            return this.mNumPostponed == 0;
        }

        public void completeTransaction() {
            boolean z = false;
            boolean canceled = this.mNumPostponed > 0;
            FragmentManagerImpl manager = this.mRecord.mManager;
            int numAdded = manager.mAdded.size();
            for (int i = 0; i < numAdded; i++) {
                Fragment fragment = manager.mAdded.get(i);
                fragment.setOnStartEnterTransitionListener((Fragment.OnStartEnterTransitionListener) null);
                if (canceled && fragment.isPostponed()) {
                    fragment.startPostponedEnterTransition();
                }
            }
            FragmentManagerImpl fragmentManagerImpl = this.mRecord.mManager;
            BackStackRecord backStackRecord = this.mRecord;
            boolean z2 = this.mIsBack;
            if (!canceled) {
                z = true;
            }
            fragmentManagerImpl.completeExecute(backStackRecord, z2, z, true);
        }

        public void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }
    }

    private static class AnimationOrAnimator {
        public final Animation animation;
        public final Animator animator;

        AnimationOrAnimator(Animation animation2) {
            this.animation = animation2;
            this.animator = null;
            if (animation2 == null) {
                throw new IllegalStateException("Animation cannot be null");
            }
        }

        AnimationOrAnimator(Animator animator2) {
            this.animation = null;
            this.animator = animator2;
            if (animator2 == null) {
                throw new IllegalStateException("Animator cannot be null");
            }
        }
    }

    private static class EndViewTransitionAnimation extends AnimationSet implements Runnable {
        private boolean mAnimating = true;
        private final View mChild;
        private boolean mEnded;
        private final ViewGroup mParent;
        private boolean mTransitionEnded;

        EndViewTransitionAnimation(Animation animation, ViewGroup parent, View child) {
            super(false);
            this.mParent = parent;
            this.mChild = child;
            addAnimation(animation);
            parent.post(this);
        }

        public boolean getTransformation(long currentTime, Transformation t) {
            this.mAnimating = true;
            if (this.mEnded) {
                return true ^ this.mTransitionEnded;
            }
            if (!super.getTransformation(currentTime, t)) {
                this.mEnded = true;
                OneShotPreDrawListener.add(this.mParent, this);
            }
            return true;
        }

        public boolean getTransformation(long currentTime, Transformation outTransformation, float scale) {
            this.mAnimating = true;
            if (this.mEnded) {
                return true ^ this.mTransitionEnded;
            }
            if (!super.getTransformation(currentTime, outTransformation, scale)) {
                this.mEnded = true;
                OneShotPreDrawListener.add(this.mParent, this);
            }
            return true;
        }

        public void run() {
            if (this.mEnded || !this.mAnimating) {
                this.mParent.endViewTransition(this.mChild);
                this.mTransitionEnded = true;
                return;
            }
            this.mAnimating = false;
            this.mParent.post(this);
        }
    }
}
