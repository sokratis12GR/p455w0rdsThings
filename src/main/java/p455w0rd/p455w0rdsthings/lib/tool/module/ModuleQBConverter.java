package p455w0rd.p455w0rdsthings.lib.tool.module;

import java.io.File;
import java.util.Arrays;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import p455w0rd.p455w0rdsthings.lib.render.QBImporter;

public class ModuleQBConverter extends JOptModule {
	
	public ModuleQBConverter() {
		this.parser.acceptsAll(Arrays.asList(new String[] {
				"?", "h", "help"
		}), "Show the help");
		this.parser.acceptsAll(Arrays.asList(new String[] {
				"i", "input"
		}), "comma separated list of paths to models (.qb or directories)").withRequiredArg().ofType(File.class)
				.withValuesSeparatedBy(',').required();
		this.parser.acceptsAll(Arrays.asList(new String[] {
				"o", "out"
		}), "Output Directory").withRequiredArg().ofType(File.class);
		this.parser.acceptsAll(Arrays.asList(new String[] {
				"o2", "textureplanes"
		}), "2nd level optimisation. Merges coplanar polygons. Increases texture size");
		this.parser.acceptsAll(Arrays.asList(new String[] {
				"s", "squaretextures"
		}), "Produce square textures");
		this.parser.acceptsAll(Arrays.asList(new String[] {
				"t", "mergetextures"
		}), "Use the same texture for all models");
		this.parser.acceptsAll(Arrays.asList(new String[] {
				"r", "scalemc"
		}), "Resize model to mc standard (shrink by factor of 16)");
	}

	protected void main(OptionParser parser, OptionSet options) {
		int flags = 0;
		if (options.has("o2")) {
			flags |= 0x1;
		}
		if (options.has("s")) {
			flags |= 0x2;
		}
		if (options.has("t")) {
			flags |= 0x4;
		}
		if (options.has("r")) {
			flags |= 0x8;
		}
		File[] input = (File[]) options.valuesOf("input").toArray(new File[0]);
		File[] outDir = new File[input.length];
		if (options.has("out")) {
			File output = (File) options.valueOf("out");
			if (output.isFile()) {
				throw new RuntimeException("Output Path is not a directory");
			}
			if (!output.exists()) {
				output.mkdirs();
			}
			for (int i = 0; i < input.length; i++) {
				outDir[i] = output;
			}
		}
		else {
			for (int i = 0; i < input.length; i++) {
				outDir[i] = (input[i].isDirectory() ? input[i] : input[i].getParentFile());
			}
		}
		for (int i = 0; i < input.length; i++) {
			File file = input[i];
			if (file.isDirectory()) {
				for (File file2 : file.listFiles()) {
					if (file2.getName().endsWith(".qb")) {
						convert(file2, outDir[i], flags);
					}
				}
			}
			else {
				convert(file, outDir[i], flags);
			}
		}
	}

	private void convert(File in, File outDir, int flags) {
		System.out.println("Converting: " + in.getName());
		QBImporter.RasterisedModel m = QBImporter.loadQB(in).toRasterisedModel(flags);
		m.export(new File(outDir, in.getName().replace(".qb", ".obj")), outDir);
	}

	public String name() {
		return "QBConverter";
	}
}
