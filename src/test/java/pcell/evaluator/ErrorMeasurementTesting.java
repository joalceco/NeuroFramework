package pcell.evaluator;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ErrorMeasurementTesting {

    DoubleMatrix2D a,b,c,d;
    MaeEvaluator eval;

    @Before
    public void setUp() throws Exception {
        a= new DenseDoubleMatrix2D(new double[][]{{0,1},{2,3}});
        b= new DenseDoubleMatrix2D(new double[][]{{0,1},{2,3}});
        c= new DenseDoubleMatrix2D(new double[][]{{1,2},{3,4}});
        d= new DenseDoubleMatrix2D(new double[][]{{2,1},{-1,2}});
        eval = new MaeEvaluator();
    }

    @Test
    public void maePerfectScore() throws Exception {
        double [] results = new double[2];
        double [] expected = new double[]{0.0,0.0};
        results[0]=eval.mae(a,b);
        results[1]=eval.mae(b,a);
        assertArrayEquals(expected,results,0.01);
    }

    @Test
    public void maeNormalErrorTesting() throws Exception {
        double [] results = new double[4];
        double [] expected = new double[]{0.0,1.0,2.0,1.5};
        results[0]=eval.mae(a,b);
        results[1]=eval.mae(b,c);
        results[2]=eval.mae(c,d);
        results[3]=eval.mae(b,d);
        assertArrayEquals(expected,results,0.01);
    }

}