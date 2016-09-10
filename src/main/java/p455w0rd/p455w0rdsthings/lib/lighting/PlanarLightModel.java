package p455w0rd.p455w0rdsthings.lib.lighting;

import p455w0rd.p455w0rdsthings.lib.color.ColourRGBA;
import p455w0rd.p455w0rdsthings.lib.render.CCRenderState;
import p455w0rd.p455w0rdsthings.lib.render.CCRenderState.IVertexOperation;

public class PlanarLightModel implements IVertexOperation {
	
	public static PlanarLightModel standardLightModel = LightModel.standardLightModel.reducePlanar();
	public int[] colours;

	public PlanarLightModel(int[] colours) {
		this.colours = colours;
	}

	public boolean load() {
		if (!CCRenderState.computeLighting) {
			return false;
		}
		CCRenderState.pipeline.addDependency(CCRenderState.sideAttrib);
		CCRenderState.pipeline.addDependency(CCRenderState.colourAttrib);
		return true;
	}

	public void operate() {
		CCRenderState.colour = ColourRGBA.multiply(CCRenderState.colour, this.colours[CCRenderState.side]);
	}

	public int operationID() {
		return LightModel.operationIndex;
	}
}
