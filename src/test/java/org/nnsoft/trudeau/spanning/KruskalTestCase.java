package org.nnsoft.trudeau.spanning;

/*
 *   Copyright 2013 The Trudeau Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.nnsoft.trudeau.spanning.SpanningTreeSolver.minimumSpanningTree;

import org.junit.Test;
import org.nnsoft.trudeau.api.Graph;
import org.nnsoft.trudeau.api.SpanningTree;
import org.nnsoft.trudeau.inmemory.MutableSpanningTree;
import org.nnsoft.trudeau.inmemory.UndirectedMutableGraph;
import org.nnsoft.trudeau.inmemory.labeled.BaseLabeledVertex;
import org.nnsoft.trudeau.inmemory.labeled.BaseLabeledWeightedEdge;
import org.nnsoft.trudeau.inmemory.labeled.BaseWeightedEdge;
import org.nnsoft.trudeau.math.monoid.primitive.DoubleWeightBaseOperations;

public final class KruskalTestCase
{

    @Test( expected = NullPointerException.class )
    public void testNullGraph()
    {
        minimumSpanningTree( (Graph<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>>) null )
            .whereEdgesHaveWeights( new BaseWeightedEdge<Double>() )
            .fromArbitrarySource()
            .applyingKruskalAlgorithm( new DoubleWeightBaseOperations() );
    }

    @Test( expected = NullPointerException.class )
    public void testNullVertex()
    {
        UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>> input =
            new UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>>();
        minimumSpanningTree( input )
            .whereEdgesHaveWeights( new BaseWeightedEdge<Double>() )
            .fromSource( null )
            .applyingKruskalAlgorithm( new DoubleWeightBaseOperations() );
    }

    @Test( expected = NullPointerException.class )
    public void testNullMonoid()
    {
        UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>> input = null;
        BaseLabeledVertex a = null;
        try
        {
            input = new UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>>();
            a = new BaseLabeledVertex( "A" );
            input.addVertex( a );
        }
        catch ( NullPointerException e )
        {
            //try..catch need to avoid a possible test success even if a NPE is thorw during graph population
            fail( e.getMessage() );
        }

        minimumSpanningTree( input )
            .whereEdgesHaveWeights( new BaseWeightedEdge<Double>() )
            .fromSource( a )
            .applyingKruskalAlgorithm( null );
    }

    @Test( expected = IllegalStateException.class )
    public void testNotExistVertex()
    {
        UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>> input =
            new UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>>();

        minimumSpanningTree( input )
            .whereEdgesHaveWeights( new BaseWeightedEdge<Double>() )
            .fromSource( new BaseLabeledVertex( "NOT EXIST" ) );
    }

    @Test( expected = IllegalStateException.class )
    public void testEmptyGraph()
    {
        UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>> input =
            new UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>>();

        minimumSpanningTree( input )
            .whereEdgesHaveWeights( new BaseWeightedEdge<Double>() )
            .fromArbitrarySource()
            .applyingKruskalAlgorithm( new DoubleWeightBaseOperations() );
    }

    /**
     * Test Graph and Prim's solution can be seen on
     * <a href="http://en.wikipedia.org/wiki/Prim%27s_algorithm">Wikipedia</a>
     */
    @Test
    public void verifyWikipediaMinimumSpanningTree()
    {
        UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>> input
            = new UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>>();

        BaseLabeledVertex a = new BaseLabeledVertex( "A" );
        BaseLabeledVertex b = new BaseLabeledVertex( "B" );
        BaseLabeledVertex c = new BaseLabeledVertex( "C" );
        BaseLabeledVertex d = new BaseLabeledVertex( "D" );
        BaseLabeledVertex e = new BaseLabeledVertex( "E" );
        BaseLabeledVertex f = new BaseLabeledVertex( "F" );
        BaseLabeledVertex g = new BaseLabeledVertex( "G" );


        input.addVertex( a );
        input.addVertex( b );
        input.addVertex( c );
        input.addVertex( d );
        input.addVertex( e );
        input.addVertex( f );
        input.addVertex( g );


        input.addEdge( a, new BaseLabeledWeightedEdge<Double>( "a <-> b", 7D ), b );
        input.addEdge( a, new BaseLabeledWeightedEdge<Double>( "a <-> d", 5D ), d );

        input.addEdge( b, new BaseLabeledWeightedEdge<Double>( "b <-> c", 8D ), c );
        input.addEdge( b, new BaseLabeledWeightedEdge<Double>( "b <-> d", 9D ), d );
        input.addEdge( b, new BaseLabeledWeightedEdge<Double>( "b <-> e", 7D ), e );

        input.addEdge( c, new BaseLabeledWeightedEdge<Double>( "c <-> e", 5D ), e );

        input.addEdge( d, new BaseLabeledWeightedEdge<Double>( "d <-> e", 15D ), e );
        input.addEdge( d, new BaseLabeledWeightedEdge<Double>( "d <-> f", 6D ), f );

        input.addEdge( e, new BaseLabeledWeightedEdge<Double>( "e <-> f", 8D ), f );
        input.addEdge( e, new BaseLabeledWeightedEdge<Double>( "e <-> g", 9D ), g );

        input.addEdge( f, new BaseLabeledWeightedEdge<Double>( "f <-> g", 11D ), g );

        // expected

        MutableSpanningTree<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>, Double> expected =
            new MutableSpanningTree<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>, Double>( new DoubleWeightBaseOperations(), new BaseWeightedEdge<Double>() );

        for ( BaseLabeledVertex vertex : input.getVertices() )
        {
            expected.addVertex( vertex );
        }

        expected.addEdge( a, new BaseLabeledWeightedEdge<Double>( "a <-> b", 7D ), b );
        expected.addEdge( a, new BaseLabeledWeightedEdge<Double>( "a <-> d", 5D ), d );
        expected.addEdge( b, new BaseLabeledWeightedEdge<Double>( "b <-> e", 9D ), e );
        expected.addEdge( c, new BaseLabeledWeightedEdge<Double>( "c <-> e", 5D ), e );
        expected.addEdge( d, new BaseLabeledWeightedEdge<Double>( "d <-> f", 6D ), f );
        expected.addEdge( e, new BaseLabeledWeightedEdge<Double>( "e <-> g", 9D ), g );

        // Actual

        SpanningTree<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>, Double> actual =
                        minimumSpanningTree( input )
                            .whereEdgesHaveWeights( new BaseWeightedEdge<Double>() )
                            .fromArbitrarySource()
                            .applyingKruskalAlgorithm( new DoubleWeightBaseOperations() );

        // assert!

        assertEquals( expected, actual );
    }


    /**
     * Test Graph and Prim's solution can be seen on
     * <a href="http://en.wikipedia.org/wiki/Prim%27s_algorithm">Wikipedia</a>
     */
    @Test
    public void verifyNotConnectedMinimumSpanningTree()
    {
        UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>> input
            = new UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>>();

        BaseLabeledVertex a = new BaseLabeledVertex( "A" );
        BaseLabeledVertex b = new BaseLabeledVertex( "B" );
        BaseLabeledVertex c = new BaseLabeledVertex( "C" );
        BaseLabeledVertex d = new BaseLabeledVertex( "D" );


        input.addVertex( a );
        input.addVertex( b );
        input.addVertex( c );
        input.addVertex( d );

        input.addEdge( a, new BaseLabeledWeightedEdge<Double>( "a <-> b", 7D ), b );


        // expected

        MutableSpanningTree<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>, Double> expected =
            new MutableSpanningTree<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>, Double>( new DoubleWeightBaseOperations(), new BaseWeightedEdge<Double>() );

        for ( BaseLabeledVertex vertex : input.getVertices() )
        {
            expected.addVertex( vertex );
        }
        expected.addEdge( a, new BaseLabeledWeightedEdge<Double>( "a <-> b", 7D ), b );

        // Actual

        SpanningTree<BaseLabeledVertex, BaseLabeledWeightedEdge<Double>, Double> actual =
                        minimumSpanningTree( input )
                            .whereEdgesHaveWeights( new BaseWeightedEdge<Double>() )
                            .fromArbitrarySource()
                            .applyingKruskalAlgorithm( new DoubleWeightBaseOperations() );

        // assert!

        assertEquals( expected, actual );
    }

}
