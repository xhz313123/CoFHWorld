package cofh.cofhworld.parser.distribution;

import cofh.cofhworld.parser.IGeneratorParser;
import cofh.cofhworld.parser.variables.BlockData;
import cofh.cofhworld.parser.variables.NumberData;
import cofh.cofhworld.util.WeightedRandomBlock;
import cofh.cofhworld.world.generator.WorldGenDecoration;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DecorationParser extends SurfaceParser implements IGeneratorParser {

	@Override
	public WorldGenerator parseGenerator(String name, Config genObject, Logger log, List<WeightedRandomBlock> resList, List<WeightedRandomBlock> matList) {

		int clusterSize = genObject.getInt("cluster-size"); // TODO: another name?
		if (clusterSize <= 0) {
			log.warn("Invalid cluster size for generator '{}'", name);
			return null;
		}

		ArrayList<WeightedRandomBlock> list = new ArrayList<>();
		ConfigObject genData = genObject.root();
		if (!genObject.hasPath("surface")) {
			log.info("Entry does not specify surface for 'decoration' generator. Using grass.");
			list.add(new WeightedRandomBlock(Blocks.GRASS));
		} else {
			if (!BlockData.parseBlockList(genData.get("surface"), list, false)) {
				log.warn("Entry specifies invalid surface for 'decoration' generator! Using grass!");
				list.clear();
				list.add(new WeightedRandomBlock(Blocks.GRASS));
			}
		}
		WorldGenDecoration r = new WorldGenDecoration(resList, clusterSize, matList, list);
		if (genObject.hasPath("see-sky")) {
			r.setSeeSky(genObject.getBoolean("see-sky"));
		}
		if (genObject.hasPath("check-stay")) {
			r.setCheckStay(genObject.getBoolean("check-stay"));
		}
		if (genObject.hasPath("stack-height")) {
			r.setStackHeight(NumberData.parseNumberValue(genData.get("stack-height")));
		}
		if (genObject.hasPath("x-variance")) {
			r.setXVar(NumberData.parseNumberValue(genData.get("x-variance"), 0, 15));
		}
		if (genObject.hasPath("y-variance")) {
			r.setYVar(NumberData.parseNumberValue(genData.get("y-variance")));
		}
		if (genObject.hasPath("z-variance")) {
			r.setZVar(NumberData.parseNumberValue(genData.get("z-variance"), 0, 15));
		}
		return r;
	}

	@Override
	protected List<WeightedRandomBlock> generateDefaultMaterial() {

		return Collections.singletonList(new WeightedRandomBlock(Blocks.GRASS, -1));
	}

	@Override
	protected String getDefaultGenerator() {

		return "decoration";
	}

}