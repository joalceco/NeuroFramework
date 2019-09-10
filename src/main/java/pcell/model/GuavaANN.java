package pcell.model;

//import cern.colt.list.tint.IntArrayList;
//import cern.colt.matrix.tdouble.DoubleMatrix1D;
//import cern.colt.matrix.tdouble.DoubleMatrix2D;
//import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
//import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import cern.colt.list.IntArrayList;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;
import utils.Data;
import utils.Functions;
import utils.G;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static GuavaANN buildANN(Path model) throws IOException {
        GuavaANN ann = new GuavaANN();
        GraphParser parser = new GraphParser(new FileInputStream(model.toFile()));
        Collection<GraphEdge> edges = parser.getEdges().values();
        int maxSize = 0;
        for(GraphEdge edge : edges){
            maxSize = Math.max(Integer.parseInt(edge.getNode1().getId()),maxSize);
            maxSize = Math.max(Integer.parseInt(edge.getNode2().getId()),maxSize);
        }
        ann.maxSize = maxSize+1;

        List<String> readAllLines = Files.readAllLines(model);
        LinkedList<String> strings = null;
        for (String line:
             readAllLines) {
            if(line.startsWith("{rank = same")){
                String[] split = line.split(";");
                strings = new LinkedList<String>(Arrays.asList(split));
                strings.removeFirst();
                strings.removeLast();
            }
        }
        for (String id:strings ) {
            int input =  Integer.parseInt(id.trim());
            ann.inputs.add(input);
            ann.topology.addNode(input);
        }
        ann.outputs.add(maxSize);
        ann.topology.addNode(maxSize);
        ann.topology.addNode(ann.bias_id);

        Collection<GraphNode> nodes = parser.getNodes().values();
        for(GraphNode node : nodes){
            int nodeid = Integer.parseInt(node.getId());
            ann.topology.addNode(nodeid);
            if(node.getAttributes().containsKey("label")){
                double bias = Double.parseDouble(((String) node.getAttributes().get("label")).split("=")[1].trim());
                ann.topology.putEdgeValue(ann.bias_id,nodeid,bias);
            }
        }

        edges = parser.getEdges().values();
        for(GraphEdge edge : edges){
            Integer n1 = Integer.parseInt(edge.getNode1().getId());
            Integer n2 = Integer.parseInt(edge.getNode2().getId());
            double weigth = Double.parseDouble((String)edge.getAttributes().get("label"));
            ann.topology.putEdgeValue(n1,n2,weigth);
//            ann.topology.putEdgeValue()
        }



//        parser.getGraphId(model.toFile())
        return ann;

    }

    public static GuavaANN buildANN(GuavaANN copy) {
        GuavaANN ann = new GuavaANN();
        ann.maxSize = copy.maxSize;
//        for (Integer node : copy.topology.nodes()) {
//            ann.topology.addNode(node);
//        }
        for (Integer input
                : copy.inputs) {
            ann.inputs.add(input);
            ann.topology.addNode(input);
        }
        for (Integer output
                : copy.outputs) {
            ann.outputs.add(output);
            ann.topology.addNode(output);
        }
        ann.topology.addNode(copy.bias_id);


        return ann;
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

    @Override
    protected void addBias(int new_node) {
        addConnection(-1, new_node);
    }

    public boolean isOutput(int neuronID) {
        return outputs.contains(neuronID);
    }

    @Override
    public Set<Integer> getActiveInputs() {
        Set<Integer> inputs = getInputs();
        Set<Integer> activeInputs = inputs.stream()
                .filter(s -> topology.degree(s) > 0)
                .collect(Collectors.toSet());
        return activeInputs;
    }

    @Override
    public boolean isActive(int id) {
        if (!topology.nodes().contains(id)) {
            return false;
        }
        return topology.degree(id) > 0;
    }

    @Override
    public int getNumberOfConnections() {
        return topology.edges().size();
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
        addConnection(originID, destinyID, G.r.nextWeight());
    }

    @Override
    public void addConnection(int originID, int destinyID, double weight) {
        topology.putEdgeValue(originID, destinyID, weight);
    }

    @Override
    public int selectRandomActiveOrInputNeuron() {
        List<Integer> neurons = getHiddenNeurons(true);
        neurons.addAll(inputs);
        return G.r.selectRandomElement(neurons);
    }

    @Override
    public int addRandomConnection() {
        int origin = selectRandomActiveOrInputNeuron();
        int destiny = selectRandomUpperNeuron(origin, true);
        addConnection(origin, destiny, G.r.nextWeight());
        return destiny;
    }

    @Override
    public int selectRandomDisableNeuron() {
        return G.r.selectRandomElement(getHiddenNeurons(false));
    }

    @Override
    public List<Integer> getHiddenNeurons() {
        return getHiddenNeurons(true);
    }

    public List<Integer> getHiddenNeurons(boolean active) {
        List<Integer> s = new ArrayList<>();
        for (Integer i
                : topology.nodes()) {
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
        return G.r.selectRandomElement(s);
    }

    @Override
    public int selectRandomUpperNeuron(int id, boolean active) {
        List<Integer> nodes = new ArrayList<>();
        for (Integer node : topology.nodes()) {
            if (node > id & !inputs.contains(node)) {
                nodes.add(node);
            }
        }
        nodes.removeAll(inputs);

        if (active) {
            return G.r.selectRandomElement(nodes);
        } else {
            List<Integer> s = new ArrayList<>();
            for (int i = id + 1; i < maxSize; i++) {
                s.add(i);
            }
            s.removeAll(nodes);
            s.removeAll(inputs);
            return G.r.selectRandomElement(s);
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
        if (nodes.contains(bias_id)) {
            nodes.remove((Integer) bias_id);
        }
//        nodes.remove(bias_id);
        if (active) {
            return G.r.selectRandomElement(nodes);
        } else {
            List<Integer> s = new ArrayList<>();
            for (int i = 0; i < id; i++) {
                s.add(i);
            }
            s.removeAll(nodes);
            return G.r.selectRandomElement(s);
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
        return topology.edgeValue(bias_id, id).get();
//        return 0;
    }

    @Override
    public IntArrayList getDestinies(int neuronID) {
        return null;
    }

    @Override
    public Set<Integer> getDestiniesSet(int neuronID) {
        return topology.successors(neuronID);
    }

    @Override
    public void setBias(int id, double bias) {
        addConnection(bias_id, id, bias);
    }

    @Override
    public void update() {

    }

    @Override
    public String toDot() {
        String ls = System.lineSeparator();
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {").append(ls);
        sb.append("{").append(ls);
        for (Integer node : topology.nodes()) {
            if (topology.hasEdgeConnecting(bias_id, node)) {
                sb.append(node).append(" [label=\" ").append(node).append("\n ");
                sb.append("b=").append(topology.edgeValue(bias_id, node).get());
                sb.append("\"]").append(ls);
            }
        }
        sb.append("}").append(ls);

        for (EndpointPair<Integer> edge
                : topology.edges()) {
            if (edge.source() != bias_id) {
                sb.append("\t");
                sb.append(edge.source()).append(" ");
                sb.append("->");
                sb.append(edge.target()).append(" ");
                sb.append("[ label=\"");
                sb.append(topology.edgeValue(edge.nodeU(), edge.nodeV()).get());
                sb.append("\" ];");
                sb.append(System.lineSeparator());
            }
        }
        sb.append("{rank = same;");
        for (int i : inputs) {
            sb.append(" " + i + ";");
        }
        sb.append("}").append(ls);

        sb.append("}");
        return sb.toString();
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
        for (EndpointPair<Integer> edge
                : topology.edges()) {
            stringBuilder.append("\t");
            stringBuilder.append(edge.source());
            stringBuilder.append("--[");
            stringBuilder.append(String.format("%.3f", topology.edgeValue(edge.nodeU(), edge.nodeV()).get()));
            stringBuilder.append("]-->");
            stringBuilder.append(edge.target());
            stringBuilder.append(System.lineSeparator());
        }
        return "GuavaANN {" + System.lineSeparator()
                + "id=" + id + System.lineSeparator()
                + "fitness=" + String.format("%.3f", fitness) + System.lineSeparator()
                + "edges=" + stringBuilder.toString()
                + ", inputs=" + inputs
                + ", outputs=" + outputs
                + ", maxSize=" + maxSize
                + System.lineSeparator()
                + '}';
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
        HashMap<Integer, DoubleMatrix1D> energy = new HashMap<>();
        //prepare bias
        energy.put(this.bias_id, new DenseDoubleMatrix1D(nElements).assign(1));
        //prepare inputs
        inputs.forEach(input_id -> energy.put(input_id, input.viewColumn(input_id)));
        //execute neurons
        for (int hidden_node_id : nodes) {
            DoubleMatrix1D output_energy = new DenseDoubleMatrix1D(nElements);
            Set<Integer> predecessors = topology.predecessors(hidden_node_id);
            for (int predecessor : predecessors) {
                double weight = topology.edgeValue(predecessor, hidden_node_id).get();
                DoubleMatrix1D in_energy = energy.get(predecessor).copy();
                output_energy.assign(in_energy, (out, in) -> out + weight * in);
            }
            if (!outputs.contains(hidden_node_id)) {
                output_energy.assign(element -> Functions.relu(element));
            }
            energy.put(hidden_node_id, output_energy);
        }
        DoubleMatrix2D output_energy = new DenseDoubleMatrix2D(nElements, outputs.size());
        int mat_index = 0;
        for (int out : outputs) {
            output_energy.viewColumn(mat_index++).assign(energy.get(out));
        }
        return new Data("output", output_energy);
    }

    @Override
    public int getNumberOfNeurons() {
        return topology.nodes().size();
    }

    @Override
    public ANN clone() {
        GuavaANN ann = new GuavaANN();
        ann.maxSize = this.maxSize;
        for (Integer node : this.topology.nodes()) {
            ann.topology.addNode(node);
        }
        for (Integer input
                : this.inputs) {
            ann.inputs.add(input);
        }
        for (Integer output
                : this.outputs) {
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
    public ANN cloneEmpty() {
        GuavaANN ann = new GuavaANN();
        ann.maxSize = this.maxSize;
        for (Integer input
                : this.inputs) {
            ann.inputs.add(input);
            ann.topology.addNode(input);
        }
        for (Integer output
                : this.outputs) {
            ann.outputs.add(output);
            ann.topology.addNode(output);
        }
        ann.topology.addNode(this.bias_id);
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

    @Override
    public EndpointPair<Integer> selectRandomWeight() {
        Set<EndpointPair<Integer>> edges = topology.edges();
        int i = G.r.nextInt(edges.size());
        int actual = 0;
        Iterator<EndpointPair<Integer>> iterator = edges.iterator();
        while (actual++ < i) {
            iterator.next();
        }
        return iterator.next();
    }

    @Override
    public Set<Integer> getNodes() {
        return this.topology.nodes();
    }

    @Override
    public boolean connectionExist(int origin, int destiny) {
        return topology.hasEdgeConnecting(origin, destiny);
    }

    @Override
    public double getWeight(int origin, int destiny) {
        return topology.edgeValue(origin, destiny).get();
    }

    @Override
    public Set<Integer> getInputs() {
        return inputs;
    }

    @Override
    public void addBias(int new_node, double bias) {
        topology.putEdgeValue(bias_id, new_node, bias);
    }

    @Override
    public Set<Integer> getPredecessorNeuronsOf(int id) {
        Set<Integer> predecessors = topology.predecessors(id);
        return predecessors;
    }

    @Override
    public Set<Integer> getSucessorNeuronsOf(int id) {
        Set<Integer> successors = topology.successors(id);
        return successors;
    }

    @Override
    public void removeConnection(int originID, int destinyID) {
        topology.removeEdge(originID, destinyID);

    }

}
