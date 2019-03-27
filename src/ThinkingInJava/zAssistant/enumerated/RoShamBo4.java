package ThinkingInJava.zAssistant.enumerated;

import static ThinkingInJava.zAssistant.enumerated.Outcome.*;

public enum RoShamBo4 implements Competitor<RoShamBo4> {
    PAPER {
        public Outcome compete(RoShamBo4 opponent) {
            return compete(ROCK, opponent);
        }
    },
    SCISSORS {
        public Outcome compete(RoShamBo4 opponent) {
            return compete(PAPER, opponent);
        }
    },
    ROCK {
        public Outcome compete(RoShamBo4 opponent) {
            return compete(SCISSORS, opponent);
        }
    };

    Outcome compete(RoShamBo4 loser, RoShamBo4 opponent) {  //第二次分发
        return (opponent == this) ? DRAW : ((opponent == loser) ? WIN : LOSE);
    }

    public static void main(String[] args) {
        RoShamBo.play(RoShamBo4.class, 20);
    }
}
