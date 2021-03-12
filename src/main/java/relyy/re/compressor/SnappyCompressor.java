package relyy.re.compressor;

import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * @Description 默认使用Snappy 压缩
 * @Created by cairuirui
 * @Date 2020/12/7
 */
public class SnappyCompressor implements Compressor {
	@Override
	public byte[] compressor(byte[] array) throws IOException {
		if (array == null) {
			return null;
		}
		return Snappy.compress(array);
	}

	@Override
	public byte[] unCompressor(byte[] array) throws IOException {
		if (array == null) {
			return null;
		}
		return Snappy.uncompress(array);
	}
}
