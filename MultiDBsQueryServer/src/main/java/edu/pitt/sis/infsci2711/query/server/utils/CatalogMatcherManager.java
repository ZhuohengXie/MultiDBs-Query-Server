package edu.pitt.sis.infsci2711.query.server.utils;

import java.io.File;
import java.util.LinkedList;

import edu.pitt.sis.infsci2711.query.server.utils.*;
import edu.pitt.sis.infsci2711.multidbs.utils.JerseyClientUtil;

//  this class is currently left an empty shell here
//  but its purpose is to check if the catalogs in the prestomatch.matcher table exists

public class CatalogMatcherManager {
	
		//restart presto server to apply all the changes
		public static void flushChanges() throws Exception
		{
				PrestoCmdManager.restart(null);
		}
	
		//returns the did list which has lost their catalog files
		public static String[] checkCatalogs()
		{
			//LinkedList<String> ids;
			
			//use JdbcMysql, execute "select did, catalog from prestomatch.matcher"		
			//while(resultset.next()): if catalog file does not exist, push did into ids
			
			//if (ids.size() !=0)
			//convert ids to String[] , return it
			
			//currently just return null, assume that no catalog files are lost.
				return null;
		}
		
		//get the catalog files back
		public static void fixLostCatalogs(String[] ids)
		{
			int ncount=ids.length;
			for(int i=0;i<ncount;i++)
			{
				//query metastore with id, get whole connection info (with dbtype, this is connectoin type)
				;
				//use CatalogFileBuilder class to rebuild the file, file title="db"+ids[i], path is in SingletonConfig
			}
		
		}
		
		//later it will be able to unregister a database from metastore, so here we have to:
		//query prestomatch.matcher for catalog name
		//remove the catalog file
		public static void remove(String did)
		{
			//"select catalog from prestomatch.matcher where did="+did
			//fname=getstring(1);
			//File f= new File(SingletonConfig.getPrestoRoot()+fname+".properties");
			//f.delete();
		}
		
		//this should only be called before starting the jetty server
		public static void removeTrash()
		{
			//remove all files with file title pattern "tmpXX_XX_XX_XXX";
		}
}
