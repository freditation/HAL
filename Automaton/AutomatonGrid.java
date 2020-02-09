package Automaton;

import HAL.GridsAndAgents.AgentGrid2D;
import HAL.GridsAndAgents.AgentSQ2Dunstackable;
import HAL.Gui.GridWindow;
import HAL.Rand;
import HAL.Util;

class AutomatonCell extends AgentSQ2Dunstackable<AutomatonGrid> {
    public void StepCell(double dieProbability, double divideProbability) {
        if (G.rng.Double() < dieProbability) {
            // cell will die
            Dispose();
            return;
        }
        if (G.rng.Double() < divideProbability) {
            // cell will divide
            int emptyNeighbours = MapEmptyHood(G.neighbourHood);
            if (emptyNeighbours > 0) {
                G.NewAgentSQ(G.neighbourHood[G.rng.Int(emptyNeighbours)]);
            }
        }
    }
}

public class AutomatonGrid extends AgentGrid2D<AutomatonCell> {
    Rand rng = new Rand();
    int[] neighbourHood = Util.VonNeumannHood(false);

    public AutomatonGrid(int gridWidth, int gridHeight) {
        super(gridWidth, gridHeight, AutomatonCell.class);
    }

    public void StepCells(double dieProbability, double divideProbability) {
        for (AutomatonCell cell : this) {
            cell.StepCell(dieProbability, divideProbability);
        }
    }

    public void DrawModel(GridWindow gridWindow) {
        for (int i = 0; i < length; i++) {
            int colour = Util.BLACK;
            if (GetAgent(i) != null) {
                colour = Util.WHITE;
            }
            gridWindow.SetPix(i, colour);
        }
    }

    public static void main(String[] args) {
        int gridWidth = 100;
        int gridHeight = 100;
        int timesteps = 1000;
        double dieProbability = 0.0;
        double divideProbability = 0.2;

        GridWindow gridWindow = new GridWindow(gridWidth, gridHeight, 1);
        AutomatonGrid model = new AutomatonGrid(gridWidth, gridHeight);

        // initialize model
        model.NewAgentSQ(model.xDim / 2, model.yDim / 2);

        for (int i = 0; i < timesteps; i++) {
            gridWindow.TickPause(100);
            // model step
            model.StepCells(dieProbability, divideProbability);

            // draw state
            model.DrawModel(gridWindow);
        }
    }
}
