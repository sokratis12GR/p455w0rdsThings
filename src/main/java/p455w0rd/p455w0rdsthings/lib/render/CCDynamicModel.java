package p455w0rd.p455w0rdsthings.lib.render;

import p455w0rd.p455w0rdsthings.lib.lighting.LC;
import p455w0rd.p455w0rdsthings.lib.render.CCRenderState.VertexAttribute;
import p455w0rd.p455w0rdsthings.lib.render.uv.UV;
import p455w0rd.p455w0rdsthings.lib.vec.Vector3;

public class CCDynamicModel implements CCRenderState.IVertexSource {
	
	public final VertexAttribute<?>[] attributes;

	public CCDynamicModel(VertexAttribute<?>... attributes) {
		this.attributes = attributes;
	}

	public Vertex5[] getVertices() {
		return new Vertex5[0];
	}

	public <T> T getAttributes(VertexAttribute<T> attr) {
		return null;
	}

	public boolean hasAttribute(VertexAttribute<?> attr) {
		for (VertexAttribute<?> a : this.attributes) {
			if (a == attr) {
				return true;
			}
		}
		return false;
	}

	public void prepareVertex() {
	}

	public CCDynamicModel pos(Vector3 pos) {
		CCRenderState.vert.vec.set(pos);
		return this;
	}

	public CCDynamicModel pos(double x, double y, double z) {
		CCRenderState.vert.vec.set(x, y, z);
		return this;
	}

	public CCDynamicModel tex(UV uv) {
		CCRenderState.vert.uv.set(uv);
		return this;
	}

	public CCDynamicModel tex(double u, double v) {
		CCRenderState.vert.uv.set(u, v);
		return this;
	}

	public CCDynamicModel vert(Vertex5 vert) {
		CCRenderState.vert.set(vert);
		return this;
	}

	public CCDynamicModel normal(Vector3 normal) {
		CCRenderState.normal.set(normal);
		return this;
	}

	public CCDynamicModel normal(double x, double y, double z) {
		CCRenderState.normal.set(x, y, z);
		return this;
	}

	public CCDynamicModel colour(int colour) {
		CCRenderState.colour = colour;
		return this;
	}

	public CCDynamicModel brightness(int brightness) {
		CCRenderState.brightness = brightness;
		return this;
	}

	public CCDynamicModel side(int side) {
		CCRenderState.side = side;
		return this;
	}

	public CCDynamicModel lightCoord(LC lc) {
		CCRenderState.lc = lc;
		return this;
	}

	public void endVertex() {
		CCRenderState.runPipeline();
		CCRenderState.writeVert();
	}
}
