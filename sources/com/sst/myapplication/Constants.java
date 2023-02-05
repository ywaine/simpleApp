package com.sst.myapplication;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo10496d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007R\u0019\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\n\n\u0002\u0010\b\u001a\u0004\b\n\u0010\u0007R\u0019\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\n\n\u0002\u0010\b\u001a\u0004\b\f\u0010\u0007R\u001a\u0010\r\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017¨\u0006\u0018"}, mo10497d2 = {"Lcom/sst/myapplication/Constants;", "", "()V", "Airtime_BundlesMenu", "", "", "getAirtime_BundlesMenu", "()[Ljava/lang/String;", "[Ljava/lang/String;", "Buy_AirtimeSubMenu", "getBuy_AirtimeSubMenu", "mainMenu", "getMainMenu", "menuTitle", "getMenuTitle", "()Ljava/lang/String;", "setMenuTitle", "(Ljava/lang/String;)V", "menuType", "", "getMenuType", "()I", "setMenuType", "(I)V", "app_debug"}, mo10498k = 1, mo10499mv = {1, 7, 1}, mo10501xi = 48)
/* compiled from: Constants.kt */
public final class Constants {
    private static final String[] Airtime_BundlesMenu = {"1. Buy Airtime", "2. Buy Data Bundles(Offers Inside)", "3. Buy Voice Bundles", "4. IControl", "00. Main Menu"};
    private static final String[] Buy_AirtimeSubMenu = {"1. For Myself", "2. For Another Number", "00. Main Menu"};
    public static final Constants INSTANCE = new Constants();
    private static final String[] mainMenu = {"1. Send money", "2. Airtime/Bundles", "3. Withdraw cash", "4. Pay Bill", "5. Payments", "6. School Fees", "7. Financial services", "8. Wewole", "9. AirtelMoney Pay", "10. My account"};
    private static String menuTitle = "";
    private static int menuType;

    private Constants() {
    }

    public final String[] getMainMenu() {
        return mainMenu;
    }

    public final String[] getAirtime_BundlesMenu() {
        return Airtime_BundlesMenu;
    }

    public final String[] getBuy_AirtimeSubMenu() {
        return Buy_AirtimeSubMenu;
    }

    public final int getMenuType() {
        return menuType;
    }

    public final void setMenuType(int i) {
        menuType = i;
    }

    public final String getMenuTitle() {
        return menuTitle;
    }

    public final void setMenuTitle(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        menuTitle = str;
    }
}
