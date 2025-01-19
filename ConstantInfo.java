public sealed class ConstantInfo
        permits ConstantClassInfo, ConstantDoubleInfo, ConstantFieldrefInfo, ConstantFloatInfo, ConstantIntegerInfo,
        ConstantInterfacerefInfo, ConstantLongInfo, ConstantMethodrefInfo, ConstantNameAndTypeInfo, ConstantStringInfo,
        ConstantUtf8Info {
    public int tag;
}

final class ConstantClassInfo extends ConstantInfo {
    public int nameIndex;

    public ConstantClassInfo(int nameIndex) {
        this.nameIndex = nameIndex;
    }
}

final class ConstantStringInfo extends ConstantInfo {
    public int stringIndex;
}

final class ConstantUtf8Info extends ConstantInfo {
    public int length;
    public byte[] bytes;

    public ConstantUtf8Info(int length, byte[] bytes) {
        this.length = length;
        this.bytes = bytes;
    }
}

final class ConstantNameAndTypeInfo extends ConstantInfo {
    public int nameIndex;
    public int descriptorIndex;
}

final class ConstantFieldrefInfo extends ConstantInfo {
    public int classIndex;
    public int nameAndTypeIndex;
}

final class ConstantMethodrefInfo extends ConstantInfo {
    public int classIndex;
    public int nameAndTypeIndex;
}

final class ConstantInterfacerefInfo extends ConstantInfo {
    public int classIndex;
    public int nameAndTypeIndex;
}

final class ConstantIntegerInfo extends ConstantInfo {
    public int value;

    public ConstantIntegerInfo(int value) {
        this.value = value;
    }
}

final class ConstantFloatInfo extends ConstantInfo {
    public float value;

    public ConstantFloatInfo(float value) {
        this.value = value;
    }
}

final class ConstantLongInfo extends ConstantInfo {
    public int highBytes;
    public int lowBytes;
    public long value;

    public ConstantLongInfo(int highBytes, int lowBytes) {
        this.highBytes = highBytes;
        this.lowBytes = lowBytes;
        this.value = ((long) highBytes << 32) + lowBytes;
    }
}

final class ConstantDoubleInfo extends ConstantInfo {
    public int highBytes;
    public int lowBytes;
    public double value;

    public ConstantDoubleInfo(int highBytes, int lowBytes) {
        long combined = ((long) highBytes << 32) + lowBytes;
        value = Double.longBitsToDouble(combined);
    }
}
