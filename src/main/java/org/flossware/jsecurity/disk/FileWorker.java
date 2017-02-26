/*
 * Copyright (C) 2017 Scot P. Floess
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.flossware.jsecurity.disk;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Works on a file.
 *
 * @author Scot P. Floess
 */
class FileWorker implements Runnable {
    static final String PREFIX = "wipe";
    static final String SUFFIX = "disk";
    static final int SIZE = 1024 * 1000000;

    static final byte[] RAW_DATA = new byte[SIZE];

    static {
        for (int index = 0; index < SIZE; index++) {
            RAW_DATA[index] = 0;
        }
    }

    final File dir;

    FileWorker(final File dir) {
        this.dir = dir;
        dir.mkdirs();
    }

    FileWorker(final String dir) {
        this(new File(dir));
    }

    /**
     * Does the actual work.
     */
    public void run() {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(File.createTempFile(PREFIX, SUFFIX, dir), "rws");
            int totalRuns = 0;

            while (true) {
                raf.write(RAW_DATA);

                if (0 == totalRuns++ % SIZE) {
                    System.err.println(Thread.currentThread().getName() + ":  [" + totalRuns + "]");
                }
            }

        } catch (final IOException ioException) {
            System.err.println(Thread.currentThread().getName() + " received IOException!  Free disk [" + dir.getFreeSpace() + "]");
        } catch (final Exception e) {
            e.printStackTrace();
        }

        try {
            raf.write(RAW_DATA, 0, (int) dir.getFreeSpace());
        } catch (final IOException ioException) {
            System.err.println(Thread.currentThread().getName() + " received IOException when finishing up!  Free disk [" + dir.getFreeSpace() + "]");
        }
    }
}
