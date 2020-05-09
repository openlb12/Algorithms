import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballEliminationUserpackage {


    private final int TEAM_SIZE;
    private final int TEAM_CONDITION_NUM;
    private final HashMap<String, Integer> nameDict;
    private String[] teams;
    private int[][] teamRounds;
    private final FlowNet net;
    private String lastEleminatedTeam;
    private Stack<String> minCut;

    public BaseballEliminationUserpackage(String filename) {
        // create a baseball division from given filename in format specified below
        In in = new In(filename);
        TEAM_SIZE = Integer.parseInt(in.readLine());
        TEAM_CONDITION_NUM = 3;
        teamRounds = new int[TEAM_SIZE][];
        nameDict = new HashMap<String, Integer>();
        teams = new String[TEAM_SIZE];
        lastEleminatedTeam = null;
        minCut = null;

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
        if (team == lastEleminatedTeam) return minCut != null;
        lastEleminatedTeam = team;
        minCut = null;
        int teamId = nameDict.get(team);
        int proWin = teamRounds[teamId][0] + teamRounds[teamId][2];
        for (int i = 0; i < TEAM_SIZE; i++) {
            if (teamRounds[i][0] > proWin) {
                minCut = new Stack<String>();
                minCut.push(teams[i]);
                return true;
            }
        }

        Stack<Integer> cut = net.minCut(teamId, proWin);
        if (cut == null) return false;
        else {
            minCut = new Stack<String>();
            for (int tm : cut) {
                minCut.push(teams[tm]);
            }
            return true;
        }
    }

    private void printNet() {
        StdOut.print(net);
    }

    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; null if not eliminated
        if (team == null) throw new IllegalArgumentException();
        if (team == lastEleminatedTeam) return minCut;
        lastEleminatedTeam = team;
        minCut = null;
        int teamId = nameDict.get(team);
        int proWin = teamRounds[teamId][0] + teamRounds[teamId][2];
        minCut = new Stack<String>();

        for (int i = 0; i < TEAM_SIZE; i++) {
            if (teamRounds[i][0] > proWin) {
                minCut.push(teams[i]);
                return minCut;
            }
        }

        for (int tm : net.minCut(teamId, proWin)) {
            minCut.push(teams[tm]);
        }
        if (minCut.isEmpty()) {
            minCut = null;
        }
        return minCut;
    }

    public static void main(String[] args) {
        BaseballEliminationUserpackage division = new BaseballEliminationUserpackage(args[0]);
        division.printNet();
//        String[] keys = {"Detroit"};
//        for (String team : keys) {
        for (String team : division.teams) {
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
    private Bag<FlowEdge>[] adjacent;
    private Bag<FlowEdge>[] teamLink;
    private boolean[] marked;
    private FlowEdge[] edgeTo;

    public FlowNet(int[][] teamRounds) {
        TEAM_SIZE = teamRounds.length;
        this.VERTICE_SIZE = 2 + TEAM_SIZE
                + (TEAM_SIZE * TEAM_SIZE - TEAM_SIZE) / 2;
        this.SOURCE = 0;
        this.TARGET = this.VERTICE_SIZE - 1;
        adjacent = (Bag<FlowEdge>[]) new Bag[this.VERTICE_SIZE];
        teamLink = (Bag<FlowEdge>[]) new Bag[this.TEAM_SIZE];
        for (int i = 0; i < VERTICE_SIZE; i++) {
            adjacent[i] = new Bag<FlowEdge>();
        }
        for (int i = 0; i < TEAM_SIZE; i++) {
            teamLink[i] = new Bag<FlowEdge>();
        }
        // add edges to source vertice
        int count = 0;
        int teamConditionNum = 3;
        int verticeStart = 1 + (TEAM_SIZE * TEAM_SIZE - TEAM_SIZE) / 2;
        for (int i = 0; i < TEAM_SIZE; i++) {
            for (int j = 0; j < i; j++) {
                count++;
                FlowEdge game = new FlowEdge(0, count, teamRounds[i][teamConditionNum + j]);
                addEdges(0, count, game);
                FlowEdge resultUp = new FlowEdge(count, verticeStart + i, teamRounds[i][teamConditionNum + j]);
                FlowEdge resultDn = new FlowEdge(count, verticeStart + j, teamRounds[i][teamConditionNum + j]);
                addEdges(count, verticeStart + i, resultUp);
                addEdges(count, verticeStart + j, resultDn);
                teamLink[i].add(game);
                teamLink[i].add(resultUp);
                teamLink[i].add(resultDn);
                teamLink[j].add(game);
                teamLink[j].add(resultUp);
                teamLink[j].add(resultDn);
            }
        }
        for (int i = 0; i < TEAM_SIZE; i++) {
            FlowEdge result = new FlowEdge(verticeStart + i, TARGET, teamRounds[i][0]);
            addEdges(verticeStart + i, TARGET, result);
            teamLink[i].add(result);
        }
    }


    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("%d %d\n", VERTICE_SIZE, edgeNum));
        for (int v = 0; v < VERTICE_SIZE; v++) {
            s.append(v + ":  ");
            for (FlowEdge e : adjacent[v]) {
//                if (e.getTo() > v) s.append(e + "  ");
                s.append(e + "  ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    private void resetEdgeResidual() {
        for (int i = 0; i < VERTICE_SIZE; i++) {
            for (FlowEdge eg : adjacent[i]) {
                eg.reset();
            }
        }
    }


    private void setEleminatedTeam(int eleminatedTeam, int proWins) {

        int teamStart = 1 + (TEAM_SIZE * TEAM_SIZE - TEAM_SIZE) / 2;

        for (int i = 0; i < teamStart; i++) {
            for (FlowEdge eg : adjacent[i]) {
                eg.reset();
            }
        }
        for (FlowEdge eg : teamLink[eleminatedTeam]) {
            eg.setNull();
        }


        for (int i = 0; i < TEAM_SIZE; i++) {
            if (i == eleminatedTeam) continue;
            for (FlowEdge eg : adjacent[teamStart + i]) {
                int end = eg.getOther(teamStart + i);
                if (end == TARGET) eg.iniResidual(proWins - eg.getCapacity());
            }
        }

//        StdOut.print(this.toString());

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
            for (FlowEdge eg : adjacent[apex]) {
                int end = eg.getOther(apex);
                if (!marked[end] && eg.getResidual(apex) > 0) {
                    edgeTo[end] = eg;
                    marked[end] = true;
                    checkPoint.enqueue(end);
                }
            }
        }
        return marked[TARGET];
    }

    public Stack<Integer> minCut(int eleminateTeam, int proWins) {
        setEleminatedTeam(eleminateTeam, proWins);
        int flow = 0;
        while (hasAugumetPath()) {

            // get bottole neck of the path

            FlowEdge edge = edgeTo[TARGET];
            int bottleNeck = edge.getResidual(edge.getOther(TARGET));
            int end = TARGET;
            int bgn = edge.getOther(TARGET);
            while (edge != null) {
                bgn = edge.getOther(end);
                int edgeflow = edge.getResidual(bgn);
                if (bottleNeck > edgeflow) {
                    bottleNeck = edgeflow;
                }
                edge = edgeTo[bgn];
                end = bgn;
            }

            flow += bottleNeck;

            // reset path residual
            edge = edgeTo[TARGET];
            end = TARGET;
            while (edge != null) {
                bgn = edge.getOther(end);
                int resiFlow = edge.getResidual(bgn) - bottleNeck;

                edge.setResidual(bgn, resiFlow);
                edge = edgeTo[bgn];
                end = bgn;
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

    public Iterable<FlowEdge> gameLinks(int v) {
        return teamLink[v];
    }

    public void addEdges(int from, int to, FlowEdge edge) {
        adjacent[from].add(edge);
        adjacent[to].add(edge);
        edgeNum++;
    }
}

class FlowEdge {
    private int from;
    private int to;
    private int capacity;
    private int residual, reverseResisual;
    private boolean isNull;

    public FlowEdge(int from, int to, int capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.residual = capacity;
        this.reverseResisual = 0;
        isNull = false;
    }

    public void reset() {
        isNull = false;
        this.residual = capacity;
        this.reverseResisual = 0;
    }

    public void setNull() {
        isNull = true;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getOther(int v) {
        if (v == from) return to;
        else if (v == to) return from;
        else throw new IllegalArgumentException(String.format("Point %d not in edge %s", v, this.toString()));
    }

    public int getCapacity() {
        return capacity;
    }

    public int getResidual(int v) {
        if (isNull) return 0;
        if (v == from) return residual;
        else if (v == to) return reverseResisual;
        else throw new IllegalArgumentException(String.format("Point %d not in edge %s", v, this.toString()));
    }


    public void setResidual(int v, int residual) {
        if (isNull) return;
        if (residual < 0) throw new IllegalArgumentException(String.format("Residual %d not illegal in edge %s",
                residual, this.toString()));
        if (v == from) {
            int del = this.residual - residual;
            this.residual = residual;
            this.reverseResisual += del;
        } else if (v == to) {
            int del = this.reverseResisual - residual;
            this.residual += del;
            this.reverseResisual = residual;
        } else {
            throw new IllegalArgumentException(String.format("Point %d not in edge %s", v, this.toString()));
        }

    }

    public void iniResidual(int residual) {
        isNull = false;
        if (residual <= 0) {
            isNull = true;
            return;
        }
        this.residual = residual;
        this.reverseResisual = 0;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        if (isNull) {
            s.append(String.format("%d->%d %d/%d, ", from, to, 0, 0));
        } else {
            s.append(String.format("%d->%d %d/%d, ", from, to, reverseResisual, residual + reverseResisual));
        }
        return s.toString();
    }

}
