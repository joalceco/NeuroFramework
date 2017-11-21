package pcell.ann;

import cern.colt.list.tint.IntArrayList;
import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import com.google.common.graph.*;
import utils.Data;
import utils.Functions;
import utils.Global;


import java.util.*;

public class GuavaANN extends ANN {


    MutableValueGraph<Integer, Double> topology;
    TreeSet<Integer> inputs;
    TreeSet<Integer> outputs;
    int maxSize;


    private GuavaANN() {
        setId();
        this.topology = ValueGraphBuilder.directed().allowsSelfLoops(false).build();
        inputs = new TreeSet<>();
        outputs = new TreeSet<>();
    }

//    private GuavaANN(MutableValueGraph<Integer, Double> topology) {
//        inputs=new ArrayList<>();
//        outputs=new ArrayList<>();
//        this.topology = ValueGraphBuilder.from(topology).directed().allowsSelfLoops(false).build();
//        for (Integer node : topology.nodes()) {
//            this.topology.addNode(node);
//        }
//        for (EndpointPair<Integer> edge : topology.edges()) {
//            Integer U = edge.nodeU();
//            Integer V = edge.nodeV();
//            this.topology.putEdgeValue(U, V, topology.edgeValue(U, V).get());
//        }
//    }

    public static GuavaANN buildANN() {
        return new GuavaANN();
    }


    public static GuavaANN buildANN(GuavaANN copy) {
        GuavaANN newCopy = new GuavaANN();
        // TODO: 8/11/17 copiar neuronas activas
        return null;
    }


    @Override
    @Deprecated
    public void buildFromTemplate(ANN copy, boolean clone) {

    }

    @Override
    public ANN buildFromFitnessFunction(int nInputs, int nOutputs) {
        return null;
    }

    @Override
    public int addRandomNeuron() {
        int new_node = selectRandomHiddenNeuron(false);
        int lower_node = selectRandomLowerNeuron(new_node, true);
        int upper_node = selectRandomUpperNeuron(new_node, true);
        addConnection(lower_node, new_node);
        addConnection(new_node, upper_node);
        addBias(new_node);
        return new_node;
    }

    private void addBias(int new_node) {
        addConnection(-1, new_node);
    }

    public boolean isOutput(int neuronID) {
        return outputs.contains(neuronID);
    }

    public boolean isHidden(int neuronID) {
        return topology.nodes().contains(neuronID) & (!inputs.contains(neuronID)) & (!outputs.contains(neuronID));
    }

    @Override
    public void addNeuron(int neuronID) {
        topology.addNode(neuronID);
    }

    @Override
    public void deleteNeuron() {
        int neuron = selectRandomHiddenNeuron(true);
        if (isHidden(neuron)) {
            deleteNeuron(neuron);
        } else {
            // TODO: 8/11/17 Error
        }
    }

    @Override
    public void deleteNeuron(int neuronID) {
        topology.removeNode(neuronID);
    }

    @Override
    public void deleteConnection() {
        int input = selectRandomActiveOrInputNeuron();
        int output = selectRandomUpperNeuron(input, true);
        deleteConnection(input, output);
    }

    @Override
    public void deleteConnection(int originID, int destinyID) {
        topology.removeEdge(originID, destinyID);
    }

    @Override
    public void addConnection() {
        int origin = selectRandomActiveOrInputNeuron();
        int destiny = selectRandomUpperNeuron(origin, true);
        addConnection(origin, destiny);
    }

    @Override
    public void addConnection(int originID, int destinyID) {
        addConnection(originID, destinyID, Global.r.nextWeight());
    }

    @Override
    public void addConnection(int originID, int destinyID, double weight) {
        topology.putEdgeValue(originID, destinyID, weight);
    }

    @Override
    public int selectRandomActiveOrInputNeuron() {
        IntArrayList intArrayList = new IntArrayList();

        return 0;
    }

    @Override
    public int addRandomConnection() {
        int origin = selectRandomActiveOrInputNeuron();
        int destiny = selectRandomUpperNeuron(origin, true);
        addConnection(origin, destiny, Global.r.nextWeight());
        return 0;
    }

    @Override
    public int selectRandomDisableNeuron() {
        return 0;
    }

    public List<Integer> getHiddenNeurons(boolean active) {
        List<Integer> s = new ArrayList<>();
        for (Integer i :
                topology.nodes()) {
            if (i > inputs.size() && i < maxSize - outputs.size()) {
                // TODO: 8/11/17 check if true
                s.add(i);
            }
        }
        if (active) {
            return s;
        } else {
            List<Integer> nos = new ArrayList<>();
            for (int i = inputs.size(); i < maxSize - outputs.size(); i++) {
                if (!s.contains(i)) {
                    nos.add(i);
                }
            }
            return nos;
        }

    }

    @Override
    public int selectRandomHiddenNeuron(boolean active) {
        List<Integer> s = getHiddenNeurons(active);
        return Global.r.selectRandomElement(s);
    }

    @Override
    public int selectRandomUpperNeuron(int id, boolean active) {
        List<Integer> nodes = new ArrayList<>();
        for (Integer node : topology.nodes()) {
            if (node > id) {
                nodes.add(node);
            }
        }
        if (active) {
            return Global.r.selectRandomElement(nodes);
        } else {
            List<Integer> s = new ArrayList<>();
            for (int i = id + 1; i < maxSize; i++) {
                s.add(i);
            }
            s.removeAll(nodes);
            return Global.r.selectRandomElement(s);
        }

    }

    @Override
    public int selectRandomLowerNeuron(int id, boolean active) {
        List<Integer> nodes = new ArrayList<>();
        for (Integer node : topology.nodes()) {
            if (node < id) {
                nodes.add(node);
            }
        }
        if (active) {
            return Global.r.selectRandomElement(nodes);
        } else {
            List<Integer> s = new ArrayList<>();
            for (int i = 0; i < id; i++) {
                s.add(i);
            }
            s.removeAll(nodes);
            return Global.r.selectRandomElement(s);
        }

    }

    @Override
    public int selectRandomActiveLowerNeurons(int id, boolean active) {
        return 0;
    }

    @Override
    public boolean isInput(int id) {
        return false;
    }

    @Override
    public double getBias(int id) {
        return 0;
    }

    @Override
    public IntArrayList getDestinies(int neuronID) {
        return null;
    }

    @Override
    public void setBias(int id, double bias) {

    }

    @Override
    public void update() {

    }

    @Override
    public String toDot() {
        return null;
    }

    @Override
    public void changeConnection() {

    }

    @Override
    public void changeConnection(int originID, int destinyID) {
        addConnection(originID, destinyID);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder().append(System.lineSeparator());
        for (EndpointPair<Integer> edge :
                topology.edges()) {
            stringBuilder.append("\t");
            stringBuilder.append(edge.source());
            stringBuilder.append("--[");
            stringBuilder.append(String.format("%.3f", topology.edgeValue(edge.nodeU(), edge.nodeV()).get()));
            stringBuilder.append("]-->");
            stringBuilder.append(edge.target());
            stringBuilder.append(System.lineSeparator());
        }
        return "GuavaANN {" + System.lineSeparator() +
                "fitness=" + String.format("%.3f",fitness) + System.lineSeparator() +
                "edges=" + stringBuilder.toString() +
                ", inputs=" + inputs +
                ", outputs=" + outputs +
                ", maxSize=" + maxSize +
                System.lineSeparator() +
                '}';
    }

    @Override
    public DoubleMatrix2D getMatrixWeights() {
        return null;
    }

    @Override
    public Data epoch(Data x) {
        DoubleMatrix2D input = x.getRawMatrix();
        int nElements = input.rows();
        TreeSet<Integer> nodes = new TreeSet(topology.nodes());
        nodes.remove(bias_id);
        nodes.removeAll(inputs);
        HashMap<Integer,DoubleMatrix1D> energy = new HashMap<>();
        //prepare bias
        energy.put(this.bias_id,new DenseDoubleMatrix1D(nElements).assign(1));
        //prepare inputs
        inputs.forEach(input_id -> energy.put(input_id, input.viewColumn(input_id)));
        //execute neurons
        for (int hidden_node_id: nodes) {
            DoubleMatrix1D output_energy = new DenseDoubleMatrix1D(nElements);
            Set<Integer> predecessors = topology.predecessors(hidden_node_id);
            for (int predecessor:predecessors) {
                double weight=topology.edgeValue(predecessor,hidden_node_id).get();
                DoubleMatrix1D in_energy = energy.get(predecessor).copy();
                output_energy.assign(in_energy,(out, in) -> out+weight*in);
            }
            if (!outputs.contains(hidden_node_id)){
                output_energy.assign(element -> Functions.sigmoid(element));
            }
            energy.put(hidden_node_id,output_energy);
        }
        DoubleMatrix2D output_energy=new DenseDoubleMatrix2D(nElements,outputs.size());
        int mat_index=0;
        for (int out :outputs) {
            output_energy.viewColumn(mat_index++).assign(energy.get(out));
        }
        return new Data("output",output_energy);
    }


    @Override
    public int getNumberOfNeurons() {
        return topology.nodes().size();
    }

    @Override
    public Object clone() {
        GuavaANN ann = new GuavaANN();
        ann.maxSize = this.maxSize;
        for (Integer node : this.topology.nodes()) {
            ann.topology.addNode(node);
        }
        for (Integer input :
                this.inputs) {
            ann.inputs.add(input);
        }
        for (Integer output :
                this.outputs) {
            ann.outputs.add(output);
        }
        for (EndpointPair<Integer> edge : this.topology.edges()) {
            Integer nodeU = edge.nodeU();
            Integer nodeV = edge.nodeV();
            Double value = this.topology.edgeValue(nodeU, nodeV).get();
            ann.addConnection(nodeU, nodeV, value);
        }
        return ann;
    }

    @Override
    public ANN buildRandomANN(int nInputs, int nOutputs, int maxSize) {
        this.maxSize = maxSize;
        for (int i = 0; i < nInputs; i++) {
            topology.addNode(i);
            inputs.add(i);
        }
        for (int i = maxSize - nOutputs; i < maxSize; i++) {
            topology.addNode(i);
            outputs.add(i);
        }
        addRandomNeuron();
        return this;
    }

    @Override
    public Set<EndpointPair<Integer>> getEdges() {
        return topology.edges();
    }

    @Override
    public double weight(Integer source, Integer target) {
        return topology.edgeValueOrDefault(source, target, 0.0);
    }
}
