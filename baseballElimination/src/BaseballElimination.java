import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballElimination {


    private final int TEAM_SIZE;
    private final int TEAM_CONDITION_NUM;
    private final HashMap<String, Integer> nameDict;
    private String[] teams;
    private int[][] teamRounds;
    private final FlowNet net;

    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified below
        In in = new In(filename);
        TEAM_SIZE = Integer.parseInt(in.readLine());
        TEAM_CONDITION_NUM = 3;
        teamRounds = new int[TEAM_SIZE][];
        nameDict = new HashMap<String, Integer>();
        teams = new String[TEAM_SIZE];

        for (int i = 0; i < TEAM_SIZE; i++) {
            String[] vars = in.readLine().trim().split("\\s{1,}");
            nameDict.put(vars[0], i);
            teams[i] = vars[0];
            teamRounds[i] = new int[TEAM_SIZE + TEAM_CONDITION_NUM];
            for (int j = 0; j < teamRounds[i].length; j++) {
                teamRounds[i][j] = Integer.parseInt(vars[j + 1]);
            }

        }
        net = new FlowNet(teamRounds);
    }


    public int numberOfTeams() {
        // number of teams
        return TEAM_SIZE;
    }

    public Iterable<String> teams() {
        // all teams
        return nameDict.keySet();
    }

    public int wins(String team) {
        // number of wins for given team
        if (team == null) throw new IllegalArgumentException();
        return teamRounds[nameDict.get(team)][0];
    }

    public int losses(String team) {
        // number of losses for given team
        if (team == null) throw new IllegalArgumentException();
        return teamRounds[nameDict.get(team)][1];
    }

    public int remaining(String team) {
        // number of remaining games for given team
        if (team == null) throw new IllegalArgumentException();
        return teamRounds[nameDict.get(team)][2];
    }

    public int against(String team1, String team2) {
        // number of remaining games between team1 and team2
        if (team1 == null || team2 == null) throw new IllegalArgumentException();
        return teamRounds[nameDict.get(team1)][TEAM_CONDITION_NUM + nameDict.get(team2)];
    }

    public boolean isEliminated(String team) {
        // is given team eliminated?
        if (team == null) throw new IllegalArgumentException();
        int teamId = nameDict.get(team);
        int proWin = teamRounds[teamId][0] + teamRounds[teamId][2];
        for (int i = 0; i < TEAM_SIZE; i++) {
            if (teamRounds[i][0] > proWin) {
                return true;
            }
        }

        Stack<Integer> minCut = net.minCut(teamId, proWin);
        if (minCut == null) return false;
        else return true;
    }

    private void printNet() {
        StdOut.print(net);
    }

    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; null if not eliminated
        if (team == null) throw new IllegalArgumentException();
        int teamId = nameDict.get(team);
        int proWin = teamRounds[teamId][0] + teamRounds[teamId][2];
        Stack<String> tms = new Stack<String>();
        for (int tm : net.minCut(teamId, proWin)) {
            tms.push(teams[tm]);
        }
        if (tms.isEmpty()) return null;
        else return tms;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        division.printNet();
        String[] keys = {"New_York"};
        for (String team : keys) {
            StdOut.println(team);
            StdOut.print(String.format("Team %s wins %d lose %d remain %d\n", team, division.wins(team),
                    division.losses(team), division.remaining(team)));
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
//            division.printNet();
        }
    }
}


class FlowNet {
    private final int SOURCE;
    private final int TARGET;
    private final int VERTICE_SIZE;
    private final int TEAM_SIZE;
    private int edgeNum;
    private Bag<FlowEdge>[] adject;
    private boolean[] marked;
    private FlowEdge[] edgeTo;

    public FlowNet(int[][] teamRounds) {
        TEAM_SIZE = teamRounds.length;
        this.VERTICE_SIZE = 2 + TEAM_SIZE
                + (TEAM_SIZE * TEAM_SIZE - TEAM_SIZE) / 2;
        this.SOURCE = 0;
        this.TARGET = this.VERTICE_SIZE - 1;
        adject = (Bag<FlowEdge>[]) new Bag[this.VERTICE_SIZE];
        for (int i = 0; i < VERTICE_SIZE; i++) {
            adject[i] = new Bag<FlowEdge>();
        }
        // add edges to source vertice
        int count = 0;
        int teamConditionNum = 3;
        int verticeStart = 1 + (TEAM_SIZE * TEAM_SIZE - TEAM_SIZE) / 2;
        for (int i = 0; i < TEAM_SIZE; i++) {
            for (int j = 0; j < i; j++) {
                count++;
                addEdges(0, count, teamRounds[i][teamConditionNum + j]);
                addEdges(count, verticeStart + i, teamRounds[i][teamConditionNum + j]);
                addEdges(count, verticeStart + j, teamRounds[i][teamConditionNum + j]);
            }
        }
        for (int i = 0; i < TEAM_SIZE; i++) {
            addEdges(verticeStart + i, TARGET, teamRounds[i][0]);
        }
    }


    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("%d %d\n", VERTICE_SIZE, edgeNum));
        for (int v = 0; v < VERTICE_SIZE; v++) {
            s.append(v + ":  ");
            for (FlowEdge e : adject[v]) {
                if (e.getTo() != v) s.append(e + "  ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    private void resetEdgeResidual() {
        for (int i = 0; i < VERTICE_SIZE; i++) {
            for (FlowEdge eg : adject[i]) {
                eg.setResidual(eg.getCapacity());
            }
        }
    }


    private void setEleminatedTeam(int eleminatedTeam, int proWins) {

        int teamStart = 1 + (TEAM_SIZE * TEAM_SIZE - TEAM_SIZE) / 2;
        for (int i = 0; i < teamStart; i++) {
            for (FlowEdge eg : adject[i]) {
                if (eg.getTo() == eleminatedTeam + teamStart) eg.setResidual(0);
                else eg.setResidual(eg.getCapacity());
            }
        }

        for (int i = 0; i < TEAM_SIZE; i++) {
            for (FlowEdge eg : adject[teamStart + i]) {
                eg.setResidual(proWins - eg.getCapacity());
            }
        }
    }


    public boolean hasAugumetPath() {
        Queue<Integer> checkPoint = new Queue<Integer>();
        marked = new boolean[VERTICE_SIZE];
        edgeTo = new FlowEdge[VERTICE_SIZE];
        checkPoint.enqueue(SOURCE);
        marked[SOURCE] = true;
        edgeTo[SOURCE] = null;
        while (!checkPoint.isEmpty()) {
            int apex = checkPoint.dequeue();
            for (FlowEdge eg : adject[apex]) {
                int end = eg.getTo();
                if (!marked[end] && eg.getResidual() > 0) {
                    edgeTo[end] = eg;
                    marked[end] = true;
                    checkPoint.enqueue(end);
                }
            }
        }
        return marked[TARGET];
    }

    public Stack<Integer> minCut(int eleminateTeam, int proWins) {
        resetEdgeResidual();
        setEleminatedTeam(eleminateTeam, proWins);
        int flow = 0;
        while (hasAugumetPath()) {

            // get bottole neck of the path
            int bottleNeck = edgeTo[TARGET].getResidual();
            FlowEdge edge = edgeTo[TARGET];
            while (edge != null) {
                if (bottleNeck > edge.getResidual()) {
                    bottleNeck = edge.getResidual();
                }
                edge = edgeTo[edge.getFrom()];
            }

            flow += bottleNeck;

            // reset path residual
            edge = edgeTo[TARGET];
            while (edge != null) {
                edge.setResidual(edge.getResidual() - bottleNeck);
                edge = edgeTo[edge.getFrom()];
            }
        }
        Stack<Integer> minCut = new Stack<Integer>();
        int verticeStart = 1 + (TEAM_SIZE * TEAM_SIZE - TEAM_SIZE) / 2;
        for (int i = 0; i < TEAM_SIZE; i++) {
            if (i == eleminateTeam) continue;
            if (marked[verticeStart + i]) minCut.push(i);
        }
        if (minCut.isEmpty()) return null;
        else return minCut;
    }

    public void addEdges(int from, int to, int capacity) {
        adject[from].add(new FlowEdge(from, to, capacity));
//        adject[to].add(new FlowEdge(from, to, capacity - capacity));
        edgeNum++;
    }
}

class FlowEdge {
    private int from;
    private int to;
    private int capacity;
    private int residual;

    public FlowEdge(int from, int to, int capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.residual = this.capacity;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getResidual() {
        return residual;
    }

    public void setResidual(int residual) {
        if (residual < 0) this.residual = 0;
        else this.residual = residual;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("%d->%d %d/%d, ", from, to, residual, capacity));
        return s.toString();
    }

}
