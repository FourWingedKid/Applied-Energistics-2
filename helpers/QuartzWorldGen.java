package appeng.helpers;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import appeng.api.AEApi;
import appeng.core.AEConfig;
import cpw.mods.fml.common.IWorldGenerator;

final public class QuartzWorldGen implements IWorldGenerator
{

	final WorldGenMinable oreNormal;
	final WorldGenMinable oreCharged;

	public QuartzWorldGen() {
		Block normal = AEApi.instance().blocks().blockQuartzOre.block();
		Block charged = AEApi.instance().blocks().blockQuartzOreCharged.block();

		if ( normal != null && charged != null )
		{
			oreNormal = new WorldGenMinable( normal, 0, AEConfig.instance.oresPerCluster, Blocks.stone );
			oreCharged = new WorldGenMinable( charged, 0, AEConfig.instance.oresPerCluster, Blocks.stone );
		}
		else
			oreNormal = oreCharged = null;
	};

	@Override
	public void generate(Random r, int chunkX, int chunkZ, World w, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		int sealevel = w.provider.getAverageGroundLevel() + 1;

		if ( oreNormal == null || oreCharged == null )
			return;

		double oreDepthMultiplier = 15 * sealevel / 64;
		int scale = (int) Math.round( r.nextGaussian() * Math.sqrt( oreDepthMultiplier ) + oreDepthMultiplier );

		for (int x = 0; x < (r.nextBoolean() ? scale * 2 : scale) / 2; ++x)
		{
			WorldGenMinable whichOre = r.nextFloat() > 0.92 ? oreCharged : oreNormal;
			int a = chunkX * 16 + r.nextInt( 22 );
			int b = r.nextInt( 40 * sealevel / 64 ) + r.nextInt( 22 * sealevel / 64 ) + 12 * sealevel / 64;
			int c = chunkZ * 16 + r.nextInt( 22 );
			whichOre.generate( w, r, a, b, c );
		}

	}
}
