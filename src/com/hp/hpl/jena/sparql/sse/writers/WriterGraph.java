/*
 * (c) Copyright 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.sse.writers;

import java.util.Iterator;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

import com.hp.hpl.jena.sparql.core.DatasetGraph;
import com.hp.hpl.jena.sparql.serializer.SerializationContext;
import com.hp.hpl.jena.sparql.sse.Tags;
import com.hp.hpl.jena.sparql.util.FmtUtils;
import com.hp.hpl.jena.sparql.util.IndentedWriter;

public class WriterGraph
{
    public static final int NL = WriterLib.NL ;
    public static final int NoNL = WriterLib.NoNL ;
    public static final int NoSP = WriterLib.NoSP ;
    
    public static void out(IndentedWriter out, Graph graph, SerializationContext sCxt)
    { out(out, graph, null, sCxt) ; }
    
    public static void out(IndentedWriter out, Graph graph, String uri, SerializationContext sCxt)
    { writeGraph(out, graph, uri, sCxt) ; }

    public static void out(IndentedWriter out, DatasetGraph dataset, SerializationContext sCxt)
    { writeDataset(out, dataset, sCxt) ; }

    // ---- Workers
    
    private static void writeDataset(IndentedWriter out, DatasetGraph ds, SerializationContext sCxt)
    {
        WriterLib.start(out, Tags.tagDataset, NL) ;
        writeGraph(out, ds.getDefaultGraph(), sCxt) ;
        for ( Iterator iter = ds.listGraphNodes() ; iter.hasNext() ; )
        {
            Node node = (Node)iter.next() ;  
            Graph g = ds.getGraph(node) ;
            writeGraph(out, g, node, sCxt) ;
        }
        WriterLib.finish(out, Tags.tagDataset) ;
    }
    
//    private static void writeModel(IndentedWriter out, Model m, SerializationContext sCxt)
//    { writeGraph(out, m.getGraph(), null, sCxt) ; }
    
    private static void writeGraph(IndentedWriter out, Graph g, SerializationContext sCxt)
    { _writeGraph(out, g,  null, sCxt) ; }

//    private static void writeGraph(IndentedWriter out, Model m, String uri, SerializationContext sCxt)
//    { writeGraph(out, m.getGraph(), uri, sCxt) ; }
    
    private static void writeGraph(IndentedWriter out, Graph g, String uri, SerializationContext sCxt)
    { _writeGraph(out, g, FmtUtils.stringForURI(uri), sCxt) ; }
    
    private static void writeGraph(IndentedWriter out, Graph g, Node node, SerializationContext sCxt)
    { _writeGraph(out, g, FmtUtils.stringForNode(node), sCxt) ; }

    private static void _writeGraph(IndentedWriter out, Graph g, String label, SerializationContext sCxt)
    {
        WriterLib.start(out, Tags.tagGraph, NoSP) ;
        if ( label != null )
        {
            out.print(" ") ;
            out.print(label) ;
        }
        out.println() ;
        out.incIndent() ;
        boolean first = true ; 
        for ( Iterator iter = g.find(Node.ANY, Node.ANY, Node.ANY) ; iter.hasNext() ; )
        {
            if ( ! first )
                out.println();
            first = false ;
            Triple triple = (Triple)iter.next();
            WriterNode.out(out, triple, sCxt) ;
        }
        out.decIndent() ;
        if ( ! first ) out.println();
        WriterLib.finish(out, Tags.tagGraph) ;
    }
}

/*
 * (c) Copyright 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */