import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * @author Jorge L. Morla
 * 
*/
public class ClassPrinter {

    private final String fileName;

    public ClassPrinter(String fileName) {
        this.fileName = fileName;
    }

    public void print() throws IOException {
        try(FileInputStream fileInput = new FileInputStream(fileName);
            DataInputStream is = new DataInputStream(fileInput)) {
                printMagicNumber(is);
                printVersion(is);
                printConstantPool(is);
        }
    }

    private void printMagicNumber(DataInputStream is) throws IOException {
        int magic = is.readInt();
        System.out.printf("Magic Number: 0x%08X%n", magic);
    }

    private void printVersion(DataInputStream is) throws IOException {
        int minorVersion = is.readUnsignedShort();
        int majorVersion = is.readUnsignedShort();
        System.out.println("Version: %d.%d".formatted(majorVersion, minorVersion));
    }

    private void printMethodref(ConstantMethodrefInfo m, ConstantInfo[] constantinfo, int index) {
        var classInfo = (ConstantClassInfo) constantinfo[m.classIndex - 1];
        var classNameInfo = (ConstantUtf8Info) constantinfo[classInfo.nameIndex -1];
        var name = new String(classNameInfo.bytes);
        var methodInfo = (ConstantNameAndTypeInfo) constantinfo[m.nameAndTypeIndex - 1];
        var methodNameInfo = (ConstantUtf8Info) constantinfo[methodInfo.nameIndex - 1];
        var methodName = new String(methodNameInfo.bytes);
        System.out.printf("\t#%d = Methodref -----> #%d.#%d ", index, m.classIndex, m.nameAndTypeIndex);
        System.out.printf("// %s.%s \n", name, methodName);
    }

    private void printClassInfo(ConstantClassInfo c, ConstantInfo[] constantinfo, int index) {
        var nameInfo = (ConstantUtf8Info) constantinfo[c.nameIndex - 1];
        var name = new String(nameInfo.bytes);
        System.out.printf("\t#%d = Class -----> #%d //%s \n", index, c.nameIndex, name);
    }

    private void printUtf8Info(ConstantUtf8Info u, ConstantInfo[] constantInfo, int index) {
        String utf8 = new String(u.bytes);
        System.out.printf("\t#%d = UTF8 -----> %s \n".formatted(index, utf8));
    }

    private void printNameAndTypeInfo(ConstantNameAndTypeInfo n, ConstantInfo[] constantinfo, int index) {
        var nameUtf8 = (ConstantUtf8Info) constantinfo[n.nameIndex - 1];
        var name = new String(nameUtf8.bytes);
        System.out.printf("\t#%d = NameAndType -----> #%d:#%d //%s: \n",
         index, n.nameIndex, n.descriptorIndex, name);
    }

    private void printConstantPool(DataInputStream is) throws IOException {
        ConstantInfo[] constantinfo = readConstantPool(is);

        int index = 1;
        System.out.println("Constant Pool:");
        for (ConstantInfo info : constantinfo) {
            switch (info) {
                case null -> {}
                case ConstantMethodrefInfo m -> printMethodref(m, constantinfo, index);
                case ConstantClassInfo c -> printClassInfo(c, constantinfo, index);
                case ConstantUtf8Info u -> printUtf8Info(u, constantinfo, index);
                case ConstantNameAndTypeInfo n -> printNameAndTypeInfo(n, constantinfo, index);
                case ConstantLongInfo l -> System.out.printf("\t#%d = Long -----> %d \n", index, l.value);
                case ConstantDoubleInfo d -> System.out.printf("\t#%d = Double -----> %f \n", index, d.value);
                default -> {}
            }
            index++;
        }
    }

    private ConstantInfo[] readConstantPool(DataInputStream is) throws IOException {
        int poolSize = is.readUnsignedShort() - 1;
        ConstantInfo[] table = new ConstantInfo[poolSize]; // holds values and references to values

        for(int i = 0; i < poolSize; i++) {
            int tag = is.readUnsignedByte();
            switch (tag) {
                case 1:
                    int length = is.readUnsignedShort();
                    byte[] utf8 = new byte[length];
                    is.read(utf8);
                    table[i] = new ConstantUtf8Info(length, utf8);
                    break;
                case 3:
                    Integer integer = is.readInt();
                    table[i] = new ConstantIntegerInfo(integer);
                    break;
                case 4:
                    Float value = is.readFloat();
                    table[i] = new ConstantFloatInfo(value);
                    break;
                case 5:
                    int lhighBytes = is.readInt();
                    int llowBytes = is.readInt();
                    table[i] = new ConstantLongInfo(lhighBytes, llowBytes);
                    break;
                case 6:
                    int dhighBytes = is.readInt();
                    int dlowBytes = is.readInt();
                    table[i] = new ConstantDoubleInfo(dhighBytes, dlowBytes);
                    break;
                case 7:
                    table[i] = new ConstantClassInfo(is.readUnsignedShort());
                    break;
                case 10:
                    var methodref = new ConstantMethodrefInfo();
                    methodref.classIndex = is.readUnsignedShort();
                    methodref.nameAndTypeIndex = is.readUnsignedShort();
                    table[i] = methodref;
                    break;
                case 12:
                    var nameAndType = new ConstantNameAndTypeInfo();
                    nameAndType.nameIndex = is.readUnsignedShort();
                    nameAndType.descriptorIndex = is.readUnsignedShort();
                    table[i] = nameAndType;
                    break;
            }
        }

        return table;
    }
}
