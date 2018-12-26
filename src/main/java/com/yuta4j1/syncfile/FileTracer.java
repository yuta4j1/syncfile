package com.yuta4j1.syncfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileTracer implements Runnable {

	private final Path from;

	private final Path to;

	public FileTracer(Path from, Path to) {
		this.from = from;
		this.to = to;
	}

	public void run() {
		if (!Files.exists(to, LinkOption.NOFOLLOW_LINKS)) {
			// コピー先ディレクトリが存在しなかった場合、ディレクトリを作成する
			if (createDir() == null) {
				// コピー先ディレクトリを作成できなかった場合、処理を終了する
				return;
			}
		}

		try {
			// ターゲットとなるファイルのコピーを行う
			Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Path createDir() {
		Path path = null;
		try {
			path = Files.createDirectories(to);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return path;

	}

}

