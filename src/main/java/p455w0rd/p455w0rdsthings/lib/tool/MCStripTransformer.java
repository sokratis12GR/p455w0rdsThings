package p455w0rd.p455w0rdsthings.lib.tool;

import java.util.Iterator;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingMethodAdapter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import p455w0rd.p455w0rdsthings.lib.asm.ASMHelper;

public class MCStripTransformer {
	
	public static class ReferenceDetector extends Remapper {
		boolean found = false;

		public String map(String typeName) {
			if ((typeName.startsWith("net/minecraft")) || (!typeName.contains("/"))) {
				this.found = true;
			}
			return typeName;
		}
	}

	public static byte[] transform(byte[] bytes) {
		ClassNode cnode = ASMHelper.createClassNode(bytes, 8);

		boolean changed = false;
		Iterator<MethodNode> it = cnode.methods.iterator();
		while (it.hasNext()) {
			MethodNode mnode = (MethodNode) it.next();
			ReferenceDetector r = new ReferenceDetector();
			mnode.accept(new RemappingMethodAdapter(mnode.access, mnode.desc, new MethodVisitor(262144) {
			}, r));
			if (r.found) {
				it.remove();
				changed = true;
			}
		}
		if (changed) {
			bytes = ASMHelper.createBytes(cnode, 0);
		}
		return bytes;
	}
}
