package game.player;

import game.TestUtils;
import game.model.*;
import game.model.projections.ExperimentationBoardProjection;

import org.junit.jupiter.api.Test;
import player.EuclideanStrategy;
import player.RiemannStrategy;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EuclideanStrategyTest {
    private DefaultBoard uniformBoard;
    private ExperimentationBoard experimentationUniformBoard;
    private ExperimentationBoardProjection experimentationUniformBoardView;
    private RiemannStrategy riemannStrategy;
    private PlayerAvatar player;
    private PublicPlayerProjection publicPlayerProjection;

    public void setup() {
        this.uniformBoard = TestUtils.createUniformBoard(false, false, true, true);
        this.experimentationUniformBoard = new DefaultExperimentationBoard(this.uniformBoard);
        this.experimentationUniformBoardView = new ExperimentationBoardProjection(this.experimentationUniformBoard);
        this.riemannStrategy = new RiemannStrategy();
        this.player = new PlayerAvatar(Color.BLUE, new Position(3, 5),
                new Position(1, 1));
        this.publicPlayerProjection = new PublicPlayerProjection(player);
    }

    @Test
    public void testGettingCandidatesInEuclideanOrder() {
        setup();
        TestEuclideanStrategy strategy = new TestEuclideanStrategy();

        List<Position> candidatesInOrder = strategy.getCandidatesInOrderExposed();
        assertEquals(new Position(3, 5), candidatesInOrder.get(0));
        assertEquals(new Position(2, 5), candidatesInOrder.get(1));
        assertEquals(new Position(3, 4), candidatesInOrder.get(2));
        assertEquals(new Position(3, 6), candidatesInOrder.get(3));
        assertEquals(new Position(4, 5), candidatesInOrder.get(4));
        assertEquals(new Position(2, 4), candidatesInOrder.get(5));
        assertEquals(new Position(2, 6), candidatesInOrder.get(6));
        assertEquals(new Position(4, 4), candidatesInOrder.get(7));
        assertEquals(new Position(4, 6), candidatesInOrder.get(8));

        for (int candIndex = 1; candIndex < candidatesInOrder.size(); candIndex++) {
            Position previousCandidate = candidatesInOrder.get(candIndex - 1);
            Position currentCandidate = candidatesInOrder.get(candIndex);
            double distance1 = Position.getEuclideanDistance(previousCandidate, new Position(3, 5));
            double distance2 = Position.getEuclideanDistance(currentCandidate, new Position(3, 5));
            if (distance1 == distance2) {
                assertTrue(new Position.RowColumnOrderComparator().compare(previousCandidate, currentCandidate) <= 0);
            }
            else {
                assertTrue(distance1 < distance2);
            }
        }
    }

    private class TestEuclideanStrategy extends EuclideanStrategy {
        public List<Position> getCandidatesInOrderExposed() {

            return this.getCandidatesInOrder(experimentationUniformBoardView,
                    experimentationUniformBoardView.getSpareTile(), publicPlayerProjection,
                    new Position(0,0));
            //ToDo fix this!
        }
    }
}
