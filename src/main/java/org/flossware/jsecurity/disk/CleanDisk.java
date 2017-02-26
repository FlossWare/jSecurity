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

/**
 * This application will fill a disk full of files containing '0'
 *
 * @author Scot P. Floess
 */
public class CleanDisk {
    final static int TOTAL_THREADS = 4;

    static String[] ensureDir(final String[] args) {
        if (0 == args.length) {
            throw new IllegalArgumentException("Must provide a directory name!");
        }

        return args;
    }

    static void wipeDir(final String dir) throws Exception {
        final Thread threads[] = new Thread[TOTAL_THREADS];

        try {
            for (int index = 0; index < TOTAL_THREADS; index++) {
                threads[index] = new Thread(new FileWorker(dir));
                threads[index].setName("WIPE THREAD [" + index + "]");
                threads[index].start();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        System.err.println("WAITING ON THREADS TO FINISH...");

        for (int index = 0; index < TOTAL_THREADS; index++) {
            System.out.println("Waiting on [" + threads[index].getName() + "]");
            threads[index].join();
        }
    }

    public static void main(final String[] args) throws Exception {
        for (final String dir : ensureDir(args)) {
            System.err.println("Wiping dir [" + dir + "]");
            wipeDir(dir);
        }
    }
}
