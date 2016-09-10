package p455w0rd.p455w0rdsthings.handlers;

import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public enum LocaleHandler {
	
	p455w0rdshield(0);

	private String prefix = "";
	private String suffix = "";

	private LocaleHandler() {
		this.prefix = "gui.tooltips.p455w0rdsthings";
	}

	private LocaleHandler(int type) {
		switch (type) {
		case 0:
			this.prefix = "item";
			this.suffix = ".name";
		}
	}

	public String getLocal() {
		return I18n.translateToLocal(this.prefix + '.' + toString() + this.suffix);
	}
}
