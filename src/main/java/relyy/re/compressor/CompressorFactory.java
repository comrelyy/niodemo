package relyy.re.compressor;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/7
 */
public class CompressorFactory {

	public static Compressor get(byte extraInfo){
		switch (extraInfo & 24){
			case 0x0:
				return new SnappyCompressor();
			default:
				return new SnappyCompressor();
		}
	}
}
