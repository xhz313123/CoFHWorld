package cofh.cofhworld.decoration.parser;

import cofh.cofhworld.decoration.IGeneratorParser;
import cofh.cofhworld.init.FeatureParser;
import cofh.cofhworld.util.WeightedRandomBlock;
import cofh.cofhworld.world.generator.WorldGenGeode;
import com.typesafe.config.Config;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class GeodeParser implements IGeneratorParser {

	@Override
	public WorldGenerator parseGenerator(String name, Config genObject, Logger log, List<WeightedRandomBlock> resList, List<WeightedRandomBlock> matList) {

		ArrayList<WeightedRandomBlock> list = new ArrayList<>();
		if (!genObject.hasPath("crust")) {
			log.info("Entry does not specify crust for 'geode' generator. Using stone.");
			list.add(new WeightedRandomBlock(Blocks.STONE));
		} else {
			if (!FeatureParser.parseResList(genObject.root().get("crust"), list, true)) {
				log.warn("Entry specifies invalid crust for 'geode' generator! Using obsidian!");
				list.clear();
				list.add(new WeightedRandomBlock(Blocks.OBSIDIAN));
			}
		}
		WorldGenGeode r = new WorldGenGeode(resList, matList, list);
		{
			if (genObject.hasPath("hollow")) {
				r.setHollow(genObject.getBoolean("hollow"));
			}
			if (genObject.hasPath("filler")) {
				list = new ArrayList<>();
				if (!FeatureParser.parseResList(genObject.getValue("filler"), list, true)) {
					log.warn("Entry specifies invalid filler for 'geode' generator! Not filling!");
				} else {
					r.setFillBlock(list);
				}
			}
		}
		return r;
	}

}
