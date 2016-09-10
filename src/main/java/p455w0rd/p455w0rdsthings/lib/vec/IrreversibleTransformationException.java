package p455w0rd.p455w0rdsthings.lib.vec;

public class IrreversibleTransformationException extends RuntimeException {

	private static final long serialVersionUID = 6114257430340638207L;
	public ITransformation<?, ?> t;

	public IrreversibleTransformationException(ITransformation<?, ?> t) {
		this.t = t;
	}

	public String getMessage() {
		return "The following transformation is irreversible:\n" + this.t;
	}
}
