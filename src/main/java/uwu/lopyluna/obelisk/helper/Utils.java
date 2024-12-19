package uwu.lopyluna.obelisk.helper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.awt.*;

@SuppressWarnings("all")
public class Utils {

    public static ResourceLocation resLoc(String modID, String path) {
        return ResourceLocation.fromNamespaceAndPath(modID, path);
    }

    public static boolean isNotFakePlayer(Player player) {
        return player != null && !(player instanceof FakePlayer);
    }

    public static int calculateValueFromAmount(int maxTime, int currentValue, int maxValue) {
        double multiplier = (double) currentValue / maxValue;
        return (int) (maxTime * multiplier);
    }

    public static double calculatePercentage(double currentValue, double maxValue, double minOutputValue, double maxOutputValue, boolean invert) {
        double ratio = Math.max(0, Math.min(currentValue / maxValue, 1));
        return Math.round((invert ? maxOutputValue - ratio * (maxOutputValue - minOutputValue) : minOutputValue + ratio * (maxOutputValue - minOutputValue)) * 1000.0) / 1000.0;
    }

    public static TagKey<Item> itemTagTagUniversal(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
    }

    public static TagKey<Item> itemTag(String modID, String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(modID, name));
    }

    public static boolean randomChance(int chance, Level level) {
        int newChance = Mth.clamp(chance, 0, 100);
        return newChance != 0 && level.getRandom().nextInt(1,  100) <= newChance;
    }

    public static boolean randomChance(double chance, Level level) {
        int newChance = Mth.clamp(((int) chance * 100), 0, 100);
        return newChance != 0 && level.getRandom().nextInt(1,  100) <= newChance;
    }

    public static boolean tickDelay(int percentage, Level level ) {
        return level.getGameTime() % percentage != 0;
    }

    public static int percentage(int value, int maxValue, boolean invert) {
        int a = (int)(((invert ? (double)maxValue - (double)value : (double)value) / (double)maxValue) * 100);
        return a;
    }

    public static int secondsToTicks(int seconds) {
        return seconds * 20;
    }

    public static float secondsToTicks(float seconds) {
        return seconds * 20.0F;
    }

    public static String percentString(int value, int maxValue, boolean invert) {
        return percentage(value, maxValue, invert) + "%";
    }

    public static int radial(int value, int maxValue, boolean invert) {
        int a = (value / maxValue);
        a %= 360;
        return a;
    }

    public static int color(Color color) {
        return color.getRGB();
    }
    public static int color(int pRed, int pGreen, int pBlue) {
        return color(new Color(pRed, pGreen, pBlue));
    }
    public static int color(String pHexcode) {
        return color(Color.decode(pHexcode));
    }

    //DONT COMPLAIN TO ME ABOUT THIS, IF YOU DISLIKE IT SO MUCH THEN MAKE A PR TO FIX IT :3
    public static String valueToTime(int pTicks, OffsetTime pOffset) {
        boolean bT = pOffset == OffsetTime.TICKS;
        boolean bS = pOffset == OffsetTime.SECONDS || bT;
        boolean bM = pOffset == OffsetTime.MINUTES || bS;
        boolean bH = pOffset == OffsetTime.HOURS || bM;
        boolean bD = pOffset == OffsetTime.DAYS || bH;
        boolean bMTH = pOffset == OffsetTime.MONTHS || bD;
        int t = pTicks;
        int s = t / 20;
        int m = s / 60;
        int h = m / 60;
        int d = h / 24;
        int mth = d / 30;
        int y = mth / 12;
        t %= 20;
        s %= 60;
        m %= 60;
        h %= 24;
        mth %= 30;
        String ticks = bT ? conversion(t, "t", d > 0 || mth > 0 || y > 0 || s > 0 || m > 0 || h > 0, pOffset == OffsetTime.TICKS) : "";
        String secs = bS ? conversion(s, "s", d > 0 || mth > 0 || y > 0 || m > 0 || h > 0, pOffset == OffsetTime.SECONDS) : "";
        String mins = bM ? conversion(m, "m", d > 0 || mth > 0 || y > 0 || h > 0, pOffset == OffsetTime.MINUTES) : "";
        String hours = bH ? conversion(h, "h", d > 0 || mth > 0 || y > 0, pOffset == OffsetTime.HOURS) : "";
        String days = bD ? conversion(d, "d", mth > 0 || y > 0, pOffset == OffsetTime.DAYS) : "";
        String months = bMTH ? conversion(mth, "m", y > 0, pOffset == OffsetTime.MONTHS) : "";
        String years = y > 0 ? y + "y" : "";
        return years + months + days + hours + mins + secs + ticks;
    }
    public static String conversion(int value, String inc, boolean above, boolean isEnding) {
        return value > 0 ? above ? value < 10 ? ":0" + value + inc : ":" + value + inc : value + inc : above ? ":00" + inc : isEnding ? "0" + inc : "";
    }
    public enum OffsetTime {
        TICKS,
        SECONDS,
        MINUTES,
        HOURS,
        DAYS,
        MONTHS,
        YEARS
    }
}
