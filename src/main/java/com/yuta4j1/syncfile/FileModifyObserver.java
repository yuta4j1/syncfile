package com.yuta4j1.syncfile;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Optional;

public class FileModifyObserver {

	private final Path observeTarget;

	private final Path copyTo;

	public FileModifyObserver(Path observeTarget, Path copyTo) {
		this.observeTarget = observeTarget;
		this.copyTo = copyTo;
	}

	public void observe() {
		// 監視サービスへの登録
		Optional<WatchService> watcherOpt = registerWatcherService();
		if (watcherOpt.isPresent()) {
			WatchService watcher = watcherOpt.get();
			WatchKey watchKey = null;
			while (true) {
				// 変更イベントを検知して通知するまでループを繰り返す
				Optional<WatchKey> watchKeyOpt = provideWatchKey(watcher);
				if (watchKeyOpt.isPresent()) {
					watchKey = watchKeyOpt.get();
					for (WatchEvent<?> event : watchKey.pollEvents()) {

						// 変更イベントを検知した場合、通知を行う
						if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
							// 別スレッドでファイルコピーする
							Thread task = new Thread(new FileTracer(observeTarget, copyTo));
							task.start();
						}

					}
				} else {
					break;
				}
			}
			if (watchKey != null) {
				watchKey.reset();
			}

		} else {
			throw new RuntimeException("WatchServiceの登録に失敗しました。");
		}

	}

	private Optional<WatchService> registerWatcherService() {

		WatchService watcher = null;
		try {
			watcher = FileSystems.getDefault().newWatchService();
			observeTarget.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(watcher);
	}

	private Optional<WatchKey> provideWatchKey(WatchService watcher) {

		WatchKey watchKey = null;
		try {
			watchKey = watcher.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return Optional.ofNullable(watchKey);
	}

}
