package p455w0rd.p455w0rdsthings.lib.vec;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TransformationList extends Transformation {
	
	private ArrayList<Transformation> transformations = new ArrayList<Transformation>();
	private Matrix4 mat;

	public TransformationList(Transformation... transforms) {
		for (Transformation t : transforms) {
			if ((t instanceof TransformationList)) {
				this.transformations.addAll(((TransformationList) t).transformations);
			}
			else {
				this.transformations.add(t);
			}
		}
		compact();
	}

	public Matrix4 compile() {
		if (this.mat == null) {
			this.mat = new Matrix4();
			for (int i = this.transformations.size() - 1; i >= 0; i--) {
				((Transformation) this.transformations.get(i)).apply(this.mat);
			}
		}
		return this.mat;
	}

	public Matrix4 reverseCompile() {
		Matrix4 mat = new Matrix4();
		for (Transformation t : this.transformations) {
			t.apply(mat);
		}
		return mat;
	}

	public void apply(Vector3 vec) {
		if (this.mat != null) {
			this.mat.apply(vec);
		}
		else {
			for (int i = 0; i < this.transformations.size(); i++) {
				((Transformation) this.transformations.get(i)).apply(vec);
			}
		}
	}

	public void applyN(Vector3 normal) {
		if (this.mat != null) {
			this.mat.applyN(normal);
		}
		else {
			for (int i = 0; i < this.transformations.size(); i++) {
				((Transformation) this.transformations.get(i)).applyN(normal);
			}
		}
	}

	public void apply(Matrix4 mat) {
		mat.multiply(compile());
	}

	public TransformationList with(Transformation t) {
		if (t.isRedundant()) {
			return this;
		}
		this.mat = null;
		if ((t instanceof TransformationList)) {
			this.transformations.addAll(((TransformationList) t).transformations);
		}
		else {
			this.transformations.add(t);
		}
		compact();
		return this;
	}

	public TransformationList prepend(Transformation t) {
		if (t.isRedundant()) {
			return this;
		}
		this.mat = null;
		if ((t instanceof TransformationList)) {
			this.transformations.addAll(0, ((TransformationList) t).transformations);
		}
		else {
			this.transformations.add(0, t);
		}
		compact();
		return this;
	}

	private void compact() {
		ArrayList<Transformation> newList = new ArrayList<Transformation>(this.transformations.size());
		Iterator<Transformation> iterator = this.transformations.iterator();
		Transformation prev = null;
		while (iterator.hasNext()) {
			Transformation t = (Transformation) iterator.next();
			if (!t.isRedundant()) {
				if (prev != null) {
					Transformation m = (Transformation) prev.merge(t);
					if (m == null) {
						newList.add(prev);
					}
					else if (m.isRedundant()) {
						t = null;
					}
					else {
						t = m;
					}
				}
				prev = t;
			}
		}
		if (prev != null) {
			newList.add(prev);
		}
		if (newList.size() < this.transformations.size()) {
			this.transformations = newList;
			this.mat = null;
		}
		if ((this.transformations.size() > 3) && (this.mat == null)) {
			compile();
		}
	}

	public boolean isRedundant() {
		return this.transformations.size() == 0;
	}

	@SideOnly(Side.CLIENT)
	public void glApply() {
		for (int i = this.transformations.size() - 1; i >= 0; i--) {
			((Transformation) this.transformations.get(i)).glApply();
		}
	}

	public Transformation inverse() {
		TransformationList rev = new TransformationList(new Transformation[0]);
		for (int i = this.transformations.size() - 1; i >= 0; i--) {
			rev.with((Transformation) ((Transformation) this.transformations.get(i)).inverse());
		}
		return rev;
	}

	public String toString() {
		String s = "";
		for (Transformation t : this.transformations) {
			s = s + "\n" + t.toString();
		}
		return s.trim();
	}
}
