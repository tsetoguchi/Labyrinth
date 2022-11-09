package game.player;

import game.model.*;
import game.TestUtils;
import game.model.projections.ExperimentationBoardProjection;
import game.model.projections.SelfPlayerProjection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import player.RiemannStrategy;
import player.TurnPlan;

import static game.model.Direction.RIGHT;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class RiemannStrategyTest {

    private StandardBoard uniformBoard;
    private ExperimentationBoard experimentationUniformBoard;
    private ExperimentationBoardProjection experimentationUniformBoardView;
    private RiemannStrategy riemannStrategy;
    private PlayerAvatar player;
    private SelfPlayerProjection selfPlayerProjection;

    @BeforeEach
    public void setup() {
        this.uniformBoard = TestUtils.createUniformBoard(false, false, true, true);
        this.experimentationUniformBoard = new StandardExperimentationBoard(this.uniformBoard);
        this.experimentationUniformBoardView = new ExperimentationBoardProjection(this.experimentationUniformBoard);
        this.riemannStrategy = new RiemannStrategy();
        this.player = new PlayerAvatar(Color.BLUE, new Position(3, 5),
                new Position(3, 5));
        this.selfPlayerProjection = new SelfPlayerProjection(player);
    }

    @Test
    public void testGettingCandidatesInRowColumnOrder() {
//        setup();
        class AnonymousRiemannStrategy extends RiemannStrategy {
            public void testCandidateOrder() {
                List<Position> candidatesInOrder = this.getCandidatesInOrder(experimentationUniformBoardView,
                        experimentationUniformBoardView.getSpareTile(), selfPlayerProjection,
                        selfPlayerProjection.getGoalPosition());

                assertEquals(selfPlayerProjection.getGoalPosition(), candidatesInOrder.get(0));
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
        StandardBoard emptyBoard = TestUtils.createUniformBoard(true, false, true, false);
        ExperimentationBoard experimentationDeadEndBoard = new StandardExperimentationBoard(emptyBoard);
        ExperimentationBoardProjection experimentationEmptyBoardView = new ExperimentationBoardProjection(experimentationDeadEndBoard);
        PlayerAvatar playerSkip = new PlayerAvatar(Color.BLUE, new Position(3, 3),
                new Position(4, 4));
        SelfPlayerProjection playerSkipView = new SelfPlayerProjection(playerSkip);
        Optional<TurnPlan> turnPlan = this.riemannStrategy.createTurnPlan(experimentationEmptyBoardView, playerSkipView,
                Optional.empty(), new Position(4, 4));
        assertTrue(turnPlan.isEmpty());
    }

    @Test
    public void testCanMoveToGoalAtStart() {
        PlayerAvatar player = new PlayerAvatar(Color.BLUE,
                new Position(2, 2), new Position(2, 0));
        SelfPlayerProjection playerView = new SelfPlayerProjection(player);
        Optional<TurnPlan> turnPlan = this.riemannStrategy.createTurnPlan(experimentationUniformBoardView, playerView,
                Optional.empty(), new Position(2, 2));
        assertFalse(turnPlan.isEmpty());
        assertEquals(Direction.LEFT, turnPlan.get().getSlideDirection());
        assertEquals(0, turnPlan.get().getSlideIndex());
        assertEquals(4, turnPlan.get().getSpareTileRotations());
        assertEquals(new Position(2, 2), turnPlan.get().getMoveDestination());
    }

    @Test
    public void testRiemannGetTurnPlanColMustShift() {
        StandardBoard board = TestUtils.createUniformBoard(true, true, false, false);
        StandardExperimentationBoard experimentationBoard = new StandardExperimentationBoard(board);
        ExperimentationBoardProjection experimentationBoardProjection = new ExperimentationBoardProjection(experimentationBoard);

        // Gem position at row6, column2
        PlayerAvatar colPlayer = new PlayerAvatar(Color.RED,
                new Position(1, 1), new Position(1, 3));
        colPlayer.setCurrentPosition(new Position(6, 0));
        SelfPlayerProjection colPlayerView = new SelfPlayerProjection(colPlayer);

        Optional<TurnPlan> turnPlan = riemannStrategy.createTurnPlan(experimentationBoardProjection, colPlayerView,
                Optional.empty(), new Position(1, 1));

        assertTrue(turnPlan.isPresent());
        assertEquals(RIGHT, turnPlan.get().getSlideDirection());
        assertEquals(6, turnPlan.get().getSlideIndex());
    }

    @Test
    public void testRiemannDoesNotUndoPreviousMove() {
        StandardBoard board = TestUtils.createUniformBoard(false, false, true, true);
        StandardExperimentationBoard experimentationBoard = new StandardExperimentationBoard(board);
        ExperimentationBoardProjection experimentationBoardProjection = new ExperimentationBoardProjection(experimentationBoard);
        PlayerAvatar bottomRightPlayer = new PlayerAvatar(Color.BLUE,
                new Position(1, 1), new Position(5, 5));
        bottomRightPlayer.setCurrentPosition(new Position(0, 3));
        SelfPlayerProjection bottomRightPlayerView = new SelfPlayerProjection(bottomRightPlayer);

        Optional<TurnPlan> turnPlanOpt = riemannStrategy.createTurnPlan(experimentationBoardProjection, bottomRightPlayerView,
                Optional.of(new SlideAndInsertRecord(RIGHT, 0, 0)), new Position(0, 0));
        assertTrue(turnPlanOpt.isPresent());
        TurnPlan turnPlan = turnPlanOpt.get();
        assertEquals(RIGHT, turnPlan.getSlideDirection());
        assertEquals(0, turnPlan.getSlideIndex());
        assertEquals(new Position(0, 0), turnPlan.getMoveDestination());
    }
}


