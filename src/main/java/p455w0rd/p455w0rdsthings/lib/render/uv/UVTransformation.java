package p455w0rd.p455w0rdsthings.lib.render.uv;

import p455w0rd.p455w0rdsthings.lib.render.CCRenderState;
import p455w0rd.p455w0rdsthings.lib.render.CCRenderState.IVertexOperation;
import p455w0rd.p455w0rdsthings.lib.vec.ITransformation;

public abstract class UVTransformation extends ITransformation<UV, UVTransformation>
		implements IVertexOperation {
	
	public static final int operationIndex = CCRenderState.registerOperation();

	public UVTransformation at(UV point) {
		return new UVTransformationList(new UVTransformation[] {
				new UVTranslation(-point.u, -point.v), this, new UVTranslation(point.u, point.v)
		});
	}

	public UVTransformationList with(UVTransformation t) {
		return new UVTransformationList(new UVTransformation[] {
				this, t
		});
	}

	public boolean load() {
		return !isRedundant();
	}

	public void operate() {
		apply(CCRenderState.vert.uv);
	}

	public int operationID() {
		return operationIndex;
	}
}
