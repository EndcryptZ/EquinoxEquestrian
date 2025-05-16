package endcrypt.equinox.equine.attributes;

import lombok.Getter;

@Getter
public enum Height {
    SIZE_0_7(0.7, 9.0),
    SIZE_0_71(0.71, 9.1),
    SIZE_0_72(0.72, 9.2),
    SIZE_0_73(0.73, 9.3),
    SIZE_0_74(0.74, 10.0),
    SIZE_0_75(0.75, 10.1),
    SIZE_0_76(0.76, 10.2),
    SIZE_0_77(0.77, 10.3),
    SIZE_0_78(0.78, 11.0),
    SIZE_0_79(0.79, 11.1),
    SIZE_0_80(0.80, 11.2),
    SIZE_0_81(0.81, 11.3),
    SIZE_0_82(0.82, 12.0),
    SIZE_0_83(0.83, 12.1),
    SIZE_0_84(0.84, 12.2),
    SIZE_0_85(0.85, 12.3),
    SIZE_0_86(0.86, 13.0),
    SIZE_0_87(0.87, 13.1),
    SIZE_0_88(0.88, 13.2),
    SIZE_0_89(0.89, 13.3),
    SIZE_0_90(0.90, 14.0),
    SIZE_0_91(0.91, 14.1),
    SIZE_0_92(0.92, 14.2),
    SIZE_0_93(0.93, 14.3),
    SIZE_0_94(0.94, 15.0),
    SIZE_0_95(0.95, 15.1),
    SIZE_0_96(0.96, 15.2),
    SIZE_0_97(0.97, 15.3),
    SIZE_0_98(0.98, 16.0),
    SIZE_0_99(0.99, 16.1),
    SIZE_1_00(1.00, 16.2),
    SIZE_1_01(1.01, 16.3),
    SIZE_1_02(1.02, 17.0),
    SIZE_1_03(1.03, 17.1),
    SIZE_1_04(1.04, 17.2),
    SIZE_1_05(1.05, 17.3),
    SIZE_1_06(1.06, 18.0),
    SIZE_1_07(1.07, 18.1),
    SIZE_1_08(1.08, 18.2),
    SIZE_1_09(1.09, 18.3),
    SIZE_1_10(1.10, 19.0),
    SIZE_1_11(1.11, 19.1),
    SIZE_1_12(1.12, 19.2),
    SIZE_1_13(1.13, 19.3),
    SIZE_1_14(1.14, 20.0),
    SIZE_1_15(1.15, 20.1),
    SIZE_1_16(1.16, 20.2),
    SIZE_1_17(1.17, 20.3),
    SIZE_1_18(1.18, 21.0);

    private final double size;
    private final double hands;

    Height(double size, double hands) {
        this.size = size;
        this.hands = hands;
    }

    public String getHandsString() {
        return hands + " hands";
    }

    // Static method to get Height from the hands
    public static Height getByHands(double hands) {
        for (Height h : Height.values()) {
            if (h.getHands() == hands) {
                return h;
            }
        }
        return null; // Return null for unknown hands
    }

    // Static method to get hands from the size
    public static double getHandsFromSize(double size) {
        for (Height h : Height.values()) {
            if (h.getSize() == size) {
                return h.getHands();
            }
        }
        return -1; // Return -1 for unknown size
    }

    // Get the next height (or null if none exists)
    public static Height getNextHeight(double size) {
        Height[] heights = Height.values();
        for (int i = 0; i < heights.length - 1; i++) {
            if (heights[i].getSize() == size) {
                return heights[i + 1]; // Return the next height
            }
        }
        return null; // No next height (if it's the last one)
    }

    // Get the previous height (or null if none exists)
    public static Height getPreviousHeight(double size) {
        Height[] heights = Height.values();
        for (int i = 1; i < heights.length; i++) {
            if (heights[i].getSize() == size) {
                return heights[i - 1]; // Return the previous height
            }
        }
        return null; // No previous height (if it's the first one)
    }
}
