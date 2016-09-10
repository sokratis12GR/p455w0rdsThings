package p455w0rd.p455w0rdsthings.lib.render;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import org.lwjgl.util.vector.Vector3f;
import p455w0rd.p455w0rdsthings.lib.lighting.LC;
import p455w0rd.p455w0rdsthings.lib.vec.Cuboid6;

@SuppressWarnings("deprecation")
public class BlockRenderer {
	public static class BlockFace implements CCRenderState.IVertexSource {
		public Vertex5[] verts = { new Vertex5(), new Vertex5(), new Vertex5(), new Vertex5() };
		public LC[] lightCoords = { new LC(), new LC(), new LC(), new LC() };
		public boolean lcComputed = false;
		public int side;

		public Vertex5[] getVertices() {
			return this.verts;
		}

		@SuppressWarnings("unchecked")
		public <T> T getAttributes(CCRenderState.VertexAttribute<T> attr) {
            return (T)(attr == CCRenderState.lightCoordAttrib && this.lcComputed ? this.lightCoords : null);
        }

		public boolean hasAttribute(CCRenderState.VertexAttribute<?> attr) {
			return (attr == CCRenderState.sideAttrib)
					|| ((attr == CCRenderState.lightCoordAttrib) && (this.lcComputed));
		}

		public void prepareVertex() {
			CCRenderState.side = this.side;
		}

		public BlockFace computeLightCoords() {
			if (!this.lcComputed) {
				for (int i = 0; i < 4; i++) {
					this.lightCoords[i].compute(this.verts[i].vec, this.side);
				}
				this.lcComputed = true;
			}
			return this;
		}

		public BlockFace loadCuboidFace(Cuboid6 c, int side) {
			double x1 = c.min.x;
			double x2 = c.max.x;
			double y1 = c.min.y;
			double y2 = c.max.y;
			double z1 = c.min.z;
			double z2 = c.max.z;
			this.side = side;
			this.lcComputed = false;
			switch (side) {
			case 0: {
				double u1 = x1;
				double v1 = z1;
				double u2 = x2;
				double v2 = z2;
				this.verts[0].set(x1, y1, z2, u1, v2, 0);
				this.verts[1].set(x1, y1, z1, u1, v1, 0);
				this.verts[2].set(x2, y1, z1, u2, v1, 0);
				this.verts[3].set(x2, y1, z2, u2, v2, 0);
				break;
			}
			case 1: {
				double u1 = x1;
				double v1 = z1;
				double u2 = x2;
				double v2 = z2;
				this.verts[0].set(x2, y2, z2, u2, v2, 1);
				this.verts[1].set(x2, y2, z1, u2, v1, 1);
				this.verts[2].set(x1, y2, z1, u1, v1, 1);
				this.verts[3].set(x1, y2, z2, u1, v2, 1);
				break;
			}
			case 2: {
				double u1 = 1.0 - x1;
				double v1 = 1.0 - y2;
				double u2 = 1.0 - x2;
				double v2 = 1.0 - y1;
				this.verts[0].set(x1, y1, z1, u1, v2, 2);
				this.verts[1].set(x1, y2, z1, u1, v1, 2);
				this.verts[2].set(x2, y2, z1, u2, v1, 2);
				this.verts[3].set(x2, y1, z1, u2, v2, 2);
				break;
			}
			case 3: {
				double u1 = x1;
				double v1 = 1.0 - y2;
				double u2 = x2;
				double v2 = 1.0 - y1;
				this.verts[0].set(x2, y1, z2, u2, v2, 3);
				this.verts[1].set(x2, y2, z2, u2, v1, 3);
				this.verts[2].set(x1, y2, z2, u1, v1, 3);
				this.verts[3].set(x1, y1, z2, u1, v2, 3);
				break;
			}
			case 4: {
				double u1 = z1;
				double v1 = 1.0 - y2;
				double u2 = z2;
				double v2 = 1.0 - y1;
				this.verts[0].set(x1, y1, z2, u2, v2, 4);
				this.verts[1].set(x1, y2, z2, u2, v1, 4);
				this.verts[2].set(x1, y2, z1, u1, v1, 4);
				this.verts[3].set(x1, y1, z1, u1, v2, 4);
				break;
			}
			case 5: {
				double u1 = 1.0 - z1;
				double v1 = 1.0 - y2;
				double u2 = 1.0 - z2;
				double v2 = 1.0 - y1;
				this.verts[0].set(x2, y1, z1, u1, v2, 5);
				this.verts[1].set(x2, y2, z1, u1, v1, 5);
				this.verts[2].set(x2, y2, z2, u2, v1, 5);
				this.verts[3].set(x2, y1, z2, u2, v2, 5);
			}
			}
			return this;
		}
	}

	public static class FullBlock implements CCRenderState.IVertexSource {
		public Vertex5[] verts = CCModel.quadModel(24).generateBlock(0, Cuboid6.full).verts;
		public LC[] lightCoords = new LC[24];

		public FullBlock() {
			for (int i = 0; i < 24; i++) {
				this.lightCoords[i] = new LC().compute(this.verts[i].vec, i / 4);
			}
		}

		public Vertex5[] getVertices() {
			return this.verts;
		}

		@SuppressWarnings("unchecked")
		public <T> T getAttributes(CCRenderState.VertexAttribute<T> attr) {
            return (T)(attr == CCRenderState.lightCoordAttrib ? this.lightCoords : null);
        }

		public boolean hasAttribute(CCRenderState.VertexAttribute<?> attr) {
			return (attr == CCRenderState.sideAttrib) || (attr == CCRenderState.lightCoordAttrib);
		}

		public void prepareVertex() {
			CCRenderState.side = CCRenderState.vertexIndex >> 2;
		}
	}

	
	public static ItemTransformVec3f thirdPersonRightTransform = new ItemTransformVec3f(
			new Vector3f(10.0F, -45.0F, 170.0F), new Vector3f(0.0F, 0.09375F, -0.171875F),
			new Vector3f(0.375F, 0.375F, 0.375F));
	public static ItemCameraTransforms blockCameraTransform = new ItemCameraTransforms(ItemTransformVec3f.DEFAULT,
			thirdPersonRightTransform, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT,
			ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT,
			ItemTransformVec3f.DEFAULT);
	public static FullBlock fullBlock = new FullBlock();

	public static void renderFullBlock(int sideMask) {
		CCRenderState.setModel(fullBlock);
		renderFaces(sideMask);
	}

	public static void renderFaces(int sideMask) {
		if (sideMask == 63) {
			return;
		}
		for (int s = 0; s < 6; s++) {
			if ((sideMask & 1 << s) == 0) {
				CCRenderState.setVertexRange(s * 4, (s + 1) * 4);
				CCRenderState.render();
			}
		}
	}

	private static BlockFace face = new BlockFace();

	public static void renderCuboid(Cuboid6 bounds, int sideMask) {
		if (sideMask == 63) {
			return;
		}
		CCRenderState.setModel(face);
		for (int s = 0; s < 6; s++) {
			if ((sideMask & 1 << s) == 0) {
				face.loadCuboidFace(bounds, s);
				CCRenderState.render();
			}
		}
	}
}
