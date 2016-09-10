package p455w0rd.p455w0rdsthings.lib.vec;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.lib.render.CCRenderState;
import p455w0rd.p455w0rdsthings.lib.render.CCRenderState.IVertexOperation;

public abstract class Transformation extends ITransformation<Vector3, Transformation> implements IVertexOperation {
	
	public static final int operationIndex = CCRenderState.registerOperation();

	public abstract void applyN(Vector3 paramVector3);

	public abstract void apply(Matrix4 paramMatrix4);

	public Transformation at(Vector3 point) {
		return new TransformationList(new Transformation[] {
				new Translation(-point.x, -point.y, -point.z), this, point.translation()
		});
	}

	public TransformationList with(Transformation t) {
		return new TransformationList(new Transformation[] {
				this, t
		});
	}

	@SideOnly(Side.CLIENT)
	public abstract void glApply();

	public boolean load() {
		CCRenderState.pipeline.addRequirement(CCRenderState.normalAttrib.operationID());
		return !isRedundant();
	}

	public void operate() {
		apply(CCRenderState.vert.vec);
		if (CCRenderState.normalAttrib.active) {
			applyN(CCRenderState.normal);
		}
	}

	public int operationID() {
		return operationIndex;
	}
}
