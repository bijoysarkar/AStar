import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStarSearchImpl implements AStarSearch {

	int[] squarePos = { 6, 7, 8, 11, 12, 15, 16, 17 };
	int[][] pos = { { 0, 2, 6, 11, 15, 20, 22 }, { 1, 3, 8, 12, 17, 21, 23 },
			{ 4, 5, 6, 7, 8, 9, 10 }, { 13, 14, 15, 16, 17, 18, 19 } };
	char[] moves = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' };

	public AStarSearchImpl() {

	}

	@Override
	public SearchResult search(String initConfig, int modeFlag) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Queue<State> openQueue = new PriorityQueue<State>(128, State.comparator);
		Queue<State> closedQueue = new PriorityQueue<State>(128,
				State.comparator);
		State initialState = new State(initConfig, 0, getHeuristicCost(
				initConfig, modeFlag), "");
		openQueue.offer(initialState);
		map.put(initialState.config, initialState.realCost);

		int count = 0;
		while (!openQueue.isEmpty()) {
			State currentState = openQueue.poll();
			count++;
			closedQueue.offer(currentState);
			if (checkGoal(currentState.config)) {
				return new SearchResult(currentState.config,
						currentState.opSequence, count);
			}

			for (char moveName : moves) {
				String successorConfig = move(currentState.config, moveName);
				State successorState = new State(successorConfig,
						currentState.realCost + 1, getHeuristicCost(
								successorConfig, modeFlag),
						currentState.opSequence + moveName);

				if (map.containsKey(successorState.config)) {
					if (map.get(successorState.config) > successorState.realCost) {
						if (closedQueue.remove(successorState)
								|| openQueue.remove(successorState)) {
							openQueue.offer(successorState);
							map.put(successorState.config,
									successorState.realCost);
						}
					}
				} else {
					openQueue.offer(successorState);
					map.put(successorState.config, successorState.realCost);
				}
			}
		}
		return null;
	}

	@Override
	public boolean checkGoal(String config) {
		char[] chars = config.toCharArray();
		if (chars[6] == chars[7] && chars[8] == chars[11]
				&& chars[12] == chars[15] && chars[16] == chars[17]
				&& chars[6] == chars[8] && chars[12] == chars[16]
				&& chars[6] == chars[12]) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String move(String config, char op) {
		boolean forward = true;
		int strip = 0;
		switch (op) {
		case 'A':
			forward = false;
		case 'F':
			strip = 0;
			break;
		case 'B':
			forward = false;
		case 'E':
			strip = 1;
			break;
		case 'G':
			forward = false;
		case 'D':
			strip = 3;
			break;
		case 'H':
			forward = false;
		case 'C':
			strip = 2;
			break;
		}

		char chars[] = config.toCharArray();
		if (forward) {
			char platform = chars[pos[strip][6]];
			for (int i = 6; i > 0; i--) {
				chars[pos[strip][i]] = chars[pos[strip][i - 1]];
			}
			chars[pos[strip][0]] = platform;
		} else {
			char platform = chars[pos[strip][0]];
			for (int i = 0; i < 6; i++) {
				chars[pos[strip][i]] = chars[pos[strip][i + 1]];
			}
			chars[pos[strip][6]] = platform;
		}
		return String.valueOf(chars);
	}

	@Override
	public int getHeuristicCost(String config, int modeFlag) {
		if (modeFlag == 1) {
			char chars[] = config.toCharArray();
			int counts[] = new int[3];
			for (int n : squarePos) {
				counts[chars[n] - '1']++;
			}
			if (counts[0] >= counts[1] && counts[0] >= counts[2]) {
				return 8 - counts[0];
			} else if (counts[1] >= counts[0] && counts[1] >= counts[2]) {
				return 8 - counts[1];
			} else {
				return 8 - counts[2];
			}
		} else if (modeFlag == 2) {
			return 0;
		} else {
			// TODO Add your code here
			return 0;
		}
	}

}
