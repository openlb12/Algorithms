import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF connection;
    private final int size;
    private final int end;
    private final int bgn;
    /**
     * designed for percolation.
     *
     * @author HF Ye
     */
    private boolean[][] siteOpen;
    private boolean[][] siteFull;
    private int count;
    private boolean isPercolate;
    //    private int siteNum;

    /**
     * create N-by-N grid, with all sites blocked.
     *
     * @author H.F. Ye
     */
    public Percolation(int perSize) {

        // create N-by-N grid, with all sites blocked
        if (perSize <= 0) {
            throw new IllegalArgumentException("Size " + perSize + " is illegal value");
        }
        size = perSize;
        siteOpen = new boolean[size][size];
        siteFull = new boolean[size][size];
        bgn = 0;
        count = 0;
        isPercolate = false;
        end = size * size + 1;
        //        siteNum = size*size;

        // initialize siteOpen and siteFull
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                siteOpen[i][j] = false;
                siteFull[i][j] = false;
            }
        }
        // initialize additional ghost sites for quick percolate check
        connection = new WeightedQuickUnionUF(size * size + 2);
        //        for(int i=1;i<size+1;i++) {
        //            connection.union(end, end-i);
        //            connection.union(bgn, bgn+i);
        //        }

    }

    /**
     * open site (row i, column j) if it is not open already.
     *
     * @author H.F. Ye
     */
    public void open(int idi, int idj) {
        idi -= 1;
        idj -= 1;
        validate(idi);
        validate(idj);
        if (isOpen(idi + 1, idj + 1)) {
            return;
        }
        siteOpen[idi][idj] = true;
        count += 1;
        int iloc = idi * size + idj + 1;
        int[] unionList = unionCrt(idi, idj);
        for (int p = 0; p < unionList.length; p++) {
            if (unionList[p] == bgn || unionList[p] == end) {
                connection.union(iloc, unionList[p]);
            }
            else if (siteOpen[(unionList[p] - 1) / size][(unionList[p] - 1) % size]) {
                connection.union(iloc, unionList[p]);
            }

        }


        if (connection.find(iloc) == connection.find(bgn)) {
            siteFull[(iloc - 1) / size][(iloc - 1) % size] = true;
            for (int p = 0; p < unionList.length; p++) {
                if (unionList[p] == bgn || unionList[p] == end) {
                    continue;
                }
                else {
                    siteFull[(unionList[p] - 1) / size][(unionList[p] - 1) % size] = siteOpen[
                            (unionList[p] - 1) / size][(unionList[p] - 1) % size];

                }
            }

        }

        isPercolate = isPercolate || connection.find(end) == connection.find(bgn);

    }

    // returns the number of open sites

    private void validate(int iloc) {
        if (iloc < 0 || iloc >= size) {
            throw new IllegalArgumentException("index " + iloc
                                                       + " is not between 0 and " + (size - 1));
        }
    }

    //    private int siteNum() {
    //        return siteNum;
    //    }

    /**
     * is site (row i, column j) open.
     *
     * @author H.F. Ye
     */
    public boolean isOpen(int idi, int idj) {
        idi -= 1;
        idj -= 1;
        validate(idi);
        validate(idj);
        return siteOpen[idi][idj];
    }
    //    private void reset() {
    //        //initialize siteOpen and siteFull
    //        for(int i=0;i<size;i++) {
    //            for(int j=0;j<size;j++) {
    //                siteOpen[i][j]=false;
    //                siteFull[i][j]=false;
    //            }
    //        }
    //        //initialize additional ghost sites for quick percolate check
    //        connection = new WeightedQuickUnionUF(size*size+2);
    //        count = 0;
    //    }

    private int[] unionCrt(int idi, int idj) {


        int[] idirt;
        int[] jdirt;
        if (idi > 0 && idi < (size - 1)) {
            idirt = new int[] { (idi - 1) * size + idj + 1, (idi + 1) * size + idj + 1 };

        }
        else if (idi == 0) {
            idirt = new int[] { (idi + 1) * size + idj + 1, bgn };
        }
        else {
            if (!isPercolate) {
                idirt = new int[] { (idi - 1) * size + idj + 1, end };
            }
            else {
                idirt = new int[] { (idi - 1) * size + idj + 1 };
            }


        }


        if (idj > 0 && idj < (size - 1)) {
            jdirt = new int[] { (idi) * size + idj, (idi) * size + idj + 2 };
        }
        else if (idj == 0) {
            jdirt = new int[] { (idi) * size + idj + 2 };
        }
        else {
            jdirt = new int[] { (idi) * size + idj };
        }

        int[] unionList = new int[idirt.length + jdirt.length];
        for (int idx = 0; idx < idirt.length; idx++) {
            unionList[idx] = idirt[idx];
        }
        for (int idx = 0; idx < jdirt.length; idx++) {
            unionList[idx + idirt.length] = jdirt[idx];
        }

        return unionList;
    }

    /**
     * returns the number of open sites.
     *
     * @author H.F. Ye
     */
    public int numberOfOpenSites() {

        return count;
    }

    /**
     * is site (row i, column j) full.
     *
     * @author H.F. Ye
     */
    public boolean isFull(int idi, int idj) {
        idi -= 1;
        idj -= 1;
        validate(idi);
        validate(idj);
        siteFull[idi][idj] = siteFull[idi][idj]
                || connection.find(idi * size + idj + 1) == connection.find(bgn);
        return siteFull[idi][idj];
    }

    /**
     * does the system percolate.
     *
     * @author H.F. Ye
     */
    public boolean percolates() {

        return isPercolate;
    }

}
