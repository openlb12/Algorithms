import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballElimination {


    private final int TEAM_SIZE;
    private final int TEAM_CONDITION_NUM;
    private final int TEAMLOC;
    private final int VORTEXNUM;
    private final HashMap<String, Integer> nameDict;
    private String[] teams;
    private int[][] teamRounds;
    private FlowNetwork net;
    private String lastEleminatedTeam;
    private Stack<String> minCut;

    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified below
        In in = new In(filename);
        TEAM_SIZE = Integer.parseInt(in.readLine());
        TEAM_CONDITION_NUM = 3;
        teamRounds = new int[TEAM_SIZE][];
        nameDict = new HashMap<String, Integer>();
        teams = new String[TEAM_SIZE];
        minCut = null;
        lastEleminatedTeam = null;
        net = null;

        int checkTeamNum = TEAM_SIZE - 1;
        TEAMLOC = 1 + (checkTeamNum * checkTeamNum - checkTeamNum) / 2;
        VORTEXNUM = 1 + TEAMLOC + checkTeamNum;

        for (int i = 0; i < TEAM_SIZE; i++) {
            String[] vars = in.readLine().trim().split("\\s{1,}");
            nameDict.put(vars[0], i);
            teams[i] = vars[0];
            teamRounds[i] = new int[TEAM_SIZE + TEAM_CONDITION_NUM];
            for (int j = 0; j < teamRounds[i].length; j++) {
                teamRounds[i][j] = Integer.parseInt(vars[j + 1]);
            }

        }

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
        if (team == null || !nameDict.containsKey(team)) throw new IllegalArgumentException();
        return teamRounds[nameDict.get(team)][0];
    }

    public int losses(String team) {
        // number of losses for given team
        if (team == null || !nameDict.containsKey(team)) throw new IllegalArgumentException();
        return teamRounds[nameDict.get(team)][1];
    }

    public int remaining(String team) {
        // number of remaining games for given team
        if (team == null || !nameDict.containsKey(team)) throw new IllegalArgumentException();
        return teamRounds[nameDict.get(team)][2];
    }

    public int against(String team1, String team2) {
        // number of remaining games between team1 and team2
        if (team1 == null || team2 == null) throw new IllegalArgumentException();
        if (!nameDict.containsKey(team1) || !nameDict.containsKey(team2)) throw new IllegalArgumentException();
        return teamRounds[nameDict.get(team1)][TEAM_CONDITION_NUM + nameDict.get(team2)];
    }

    public boolean isEliminated(String team) {
        // is given team eliminated?
        if (team == null || !nameDict.containsKey(team)) throw new IllegalArgumentException();
        if (team.equals(lastEleminatedTeam)) return minCut != null;
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

        constructNet(team);

        FordFulkerson maxflow = new FordFulkerson(net, 0, VORTEXNUM - 1);
        int count = 0;
        minCut = new Stack<String>();
        for (int i = 0; i < TEAM_SIZE; i++) {
            if (i == teamId) continue;
            if (maxflow.inCut(count + TEAMLOC)) {
                minCut.push(teams[i]);
            }
            count++;
        }
        net = null;
        if (minCut.isEmpty()) {
            minCut = null;
            return false;
        } else {
            return true;
        }

    }

    private void constructNet(String team) {

        int count = 0;
        int teamId = nameDict.get(team);
        int proWin = teamRounds[teamId][0] + teamRounds[teamId][2];
        net = new FlowNetwork(VORTEXNUM);
        for (int i = 0; i < TEAM_SIZE; i++) {
            if (i == teamId) continue;
            for (int j = 0; j < i; j++) {
                if (j == teamId) continue;
                count++;
                FlowEdge origin = new FlowEdge(0, count, teamRounds[i][TEAM_CONDITION_NUM + j]);
                net.addEdge(origin);
                int tmA = i;
                int tmB = j;
                if (i > teamId) tmA--;
                if (j > teamId) tmB--;
                FlowEdge resultUp = new FlowEdge(count, TEAMLOC + tmA, teamRounds[i][TEAM_CONDITION_NUM + j]);
                FlowEdge resultDn = new FlowEdge(count, TEAMLOC + tmB, teamRounds[i][TEAM_CONDITION_NUM + j]);
                net.addEdge(resultUp);
                net.addEdge(resultDn);
            }
        }
        count = 0;
        for (int i = 0; i < TEAM_SIZE; i++) {
            if (i == teamId) continue;
            net.addEdge(new FlowEdge(TEAMLOC + count, VORTEXNUM - 1, proWin - teamRounds[i][0]));
            count++;
        }
    }


    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; null if not eliminated
        if (team == null || !nameDict.containsKey(team)) throw new IllegalArgumentException();
        if (team.equals(lastEleminatedTeam)) return minCut;
        lastEleminatedTeam = team;
        if (isEliminated(team)) return minCut;
        else return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
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
