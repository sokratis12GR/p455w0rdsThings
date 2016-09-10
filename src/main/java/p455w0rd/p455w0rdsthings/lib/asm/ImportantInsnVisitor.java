package p455w0rd.p455w0rdsthings.lib.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

public class ImportantInsnVisitor
  extends ClassVisitor
{
  public class ImportantInsnMethodVisitor
    extends MethodVisitor
  {
    MethodVisitor delegate;
    
    public ImportantInsnMethodVisitor(int access, String name, String desc, String signature, String[] exceptions)
    {
      super(Opcodes.ASM4, new MethodNode(access, name, desc, signature, exceptions));
      this.delegate = ImportantInsnVisitor.this.cv.visitMethod(access, name, desc, signature, exceptions);
    }
    
    public void visitEnd()
    {
      super.visitEnd();
      MethodNode mnode = (MethodNode)this.mv;
      mnode.instructions = InsnComparator.getImportantList(mnode.instructions);
      mnode.accept(this.delegate);
    }
  }
  
  public ImportantInsnVisitor(ClassVisitor cv)
  {
    super(262144, cv);
  }
  
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
  {
    return new ImportantInsnMethodVisitor(access, name, desc, signature, exceptions);
  }
}


