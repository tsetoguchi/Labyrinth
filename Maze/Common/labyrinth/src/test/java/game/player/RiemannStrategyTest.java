package game.player;

import game.model.*;
import game.TestUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import player.RiemannStrategy;
import player.Turn;

import static game.model.Direction.RIGHT;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class RiemannStrategyTest {

    private DefaultBoard uniformBoard;
    private ExperimentationBoard experimentationUniformBoard;
    private ExperimentationBoardProjection experimentationUniformBoardView;
    private RiemannStrategy riemannStrategy;
    private PlayerAvatar player;
    private PublicPlayerProjection publicPlayerProjection;

    @BeforeEach
    public void setup() {
        this.uniformBoard = TestUtils.createUniformBoard(false, false, true, true);
        this.experimentationUniformBoard = new DefaultExperimentationBoard(this.uniformBoard);
        this.experimentationUniformBoardView = new ExperimentationBoardProjection(this.experimentationUniformBoard);
        this.riemannStrategy = new RiemannStrategy();
        this.player = new PlayerAvatar(Color.BLUE, new Position(3, 5),
                new Position(3, 5));
        this.publicPlayerProjection = new PublicPlayerProjection(player);
    }

    @Test
    public void testGettingCandidatesInRowColumnOrder() {
//        setup();
        class AnonymousRiemannStrategy extends RiemannStrategy {
            public void testCandidateOrder() {
                List<Position> candidatesInOrder = this.getCandidatesInOrder(experimentationUniformBoardView,
                    publicPlayerProjection.getGoalPosition());

                assertEquals(publicPlayerProjection.getGoalPosition(), candidatesInOrder.get(0));
                int i = 1;
                for (int row = 0; row < experimentationUniformBoardView.getHeight(); row++) {
                    for (int col = 0; col < experimentationUniformBoardView.getWidth(); col++) {
                        if (row == 3 & col == 5) {
                            continue;
                        }
                        assertEquals(candidatesInOrder.get(i), new Position(row, col));
                        i++;
                    }
                }
            }
        }
        new AnonymousRiemannStrategy().testCandidateOrder();
    }

    @Test
    public void testRiemannGetTurnPlanSkip() {
        DefaultBoard emptyBoard = TestUtils.createUniformBoard(true, false, true, false);
        ExperimentationBoard experimentationDeadEndBoard = new DefaultExperimentationBoard(emptyBoard);
        ExperimentationBoardProjection experimentationEmptyBoardView = new ExperimentationBoardProjection(experimentationDeadEndBoard);
        PlayerAvatar playerSkip = new PlayerAvatar(Color.BLUE, new Position(3, 3),
                new Position(4, 4));
        PublicPlayerProjection playerSkipView = new PublicPlayerProjection(playerSkip);
        Optional<Turn> turnPlan = this.riemannStrategy.createTurnPlan(,
            experimentationEmptyBoardView,
            new Position(4, 4));
        assertTrue(turnPlan.isEmpty());
    }

    @Test
    public void testCanMoveToGoalAtStart() {
        PlayerAvatar player = new PlayerAvatar(Color.BLUE,
                new Position(2, 2), new Position(2, 0));
        PublicPlayerProjection playerView = new PublicPlayerProjection(player);
        Optional<Turn> turnPlan = this.riemannStrategy.createTurnPlan(,
            experimentationUniformBoardView,
            new Position(2, 2));
        assertFalse(turnPlan.isEmpty());
        assertEquals(Direction.LEFT, turnPlan.get().getSlideDirection());
        assertEquals(0, turnPlan.get().getSlideIndex());
        assertEquals(4, turnPlan.get().getSpareTileRotations());
        assertEquals(new Position(2, 2), turnPlan.get().getMoveDestination());
    }

    @Test
    public void testRiemannGetTurnPlanColMustShift() {
        DefaultBoard board = TestUtils.createUniformBoard(true, true, false, false);
        DefaultExperimentationBoard experimentationBoard = new DefaultExperimentationBoard(board);
        ExperimentationBoardProjection experimentationBoardProjection = new ExperimentationBoardProjection(experimentationBoard);

        // Gem position at row6, column2
        PlayerAvatar colPlayer = new PlayerAvatar(Color.RED,
                new Position(1, 1), new Position(1, 3));
        colPlayer.setCurrentPosition(new Position(6, 0));
        PublicPlayerProjection colPlayerView = new PublicPlayerProjection(colPlayer);

        Optional<Turn> turnPlan = riemannStrategy.createTurnPlan(, experimentationBoardProjection,
            new Position(1, 1));

        assertTrue(turnPlan.isPresent());
        assertEquals(RIGHT, turnPlan.get().getSlideDirection());
        assertEquals(6, turnPlan.get().getSlideIndex());
    }

    @Test
    public void testRiemannDoesNotUndoPreviousMove() {
        DefaultBoard board = TestUtils.createUniformBoard(false, false, true, true);
        DefaultExperimentationBoard experimentationBoard = new DefaultExperimentationBoard(board);
        ExperimentationBoardProjection experimentationBoardProjection = new ExperimentationBoardProjection(experimentationBoard);
        PlayerAvatar bottomRightPlayer = new PlayerAvatar(Color.BLUE,
                new Position(1, 1), new Position(5, 5));
        bottomRightPlayer.setCurrentPosition(new Position(0, 3));
        PublicPlayerProjection bottomRightPlayerView = new PublicPlayerProjection(bottomRightPlayer);

        Optional<Turn> turnPlanOpt = riemannStrategy.createTurnPlan(, experimentationBoardProjection,
            new Position(0, 0));
        assertTrue(turnPlanOpt.isPresent());
        Turn turn = turnPlanOpt.get();
        assertEquals(RIGHT, turn.getSlideDirection());
        assertEquals(0, turn.getSlideIndex());
        assertEquals(new Position(0, 0), turn.getMoveDestination());
    }
}


