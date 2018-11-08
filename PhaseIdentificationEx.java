import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
public class PhaseIdentificationEx {
	public static void main(String[] args){
		exvolset("C:\\Users\\mayi\\Desktop\\论文二次数据-图表\\实验数据\\55user_voltagedataset_1day.csv",
				"C:\\Users\\mayi\\Desktop\\论文二次数据-图表\\实验数据\\55-24h-60m.csv",1,60);
		int [] a0b1c2= {0,1,0,0,0,1,1,2,0,1,1,2,1,0,1,2,2,2,2,0,0,0,1,2,0,1,2,2,0,0,0,2,2,0,1,1,1,1,2,1,1,2,2,1,1,0,2,0,0,1,0,0,1,0,0};
		int [] a0b2c1= {0,2,0,0,0,2,2,1,0,2,2,1,2,0,2,1,1,1,1,0,0,0,2,1,0,2,1,1,0,0,0,1,1,0,2,2,2,2,1,2,2,1,1,2,2,0,1,0,0,2,0,0,2,0,0};
		int [] a1b0c2= {1,0,1,1,1,0,0,2,1,0,0,2,0,1,0,2,2,2,2,1,1,1,0,2,1,0,2,2,1,1,1,2,2,1,0,0,0,0,2,0,0,2,2,0,0,1,2,1,1,0,1,1,0,1,1};
		int [] a1b2c0= {1,2,1,1,1,2,2,0,1,2,2,0,2,1,2,0,0,0,0,1,1,1,2,0,1,2,0,0,1,1,1,0,0,1,2,2,2,2,0,2,2,0,0,2,2,1,0,1,1,2,1,1,2,1,1};
		int [] a2b0c1= {2,0,2,2,2,0,0,1,2,0,0,1,0,2,0,1,1,1,1,2,2,2,0,1,2,0,1,1,2,2,2,1,1,2,0,0,0,0,1,0,0,1,1,0,0,2,1,2,2,0,2,2,0,2,2};
		int [] a2b1c0= {2,1,2,2,2,1,1,0,2,1,1,0,1,2,1,0,0,0,0,2,2,2,1,0,2,1,0,0,2,2,2,0,0,2,1,1,1,1,0,1,1,0,0,1,1,2,0,2,2,1,2,2,1,2,2};
		int[]input={0,2,0,0,0,2,2,1,0,2,2,1,2,0,2,1,1,1,1,0,0,0,2,1,0,2,1,1,0,0,0,1,1,0,2,2,2
				,2,1,2,2,1,1,2,2,0,1,0,0,2,0,0,2,0,0};
		/*int [] acc= {};
		 * 
		 */
		System.out.println(accuracy(a0b2c1,input));
	}
	
	public static double accuracy(int [] source,int [] input) {
		int correctnum=0;
		for(int i=0;i<source.length;i++) {
			if(source[i] == input[i]) {
				correctnum++;
			}
		}
		return correctnum/55.0;
	}
	
	/**
	 * @param inpath 输入文件地址
	 * @param outpath 输出文件地址
	 * @param allratio 总体的输入比例，例如：1/2表示只使用原始数据集的前1/2
	 * @param sigratio 采样比例，若为1表示每一个取一次，5：每五个取一次，15：每15个取一次
	 */
	public static void exvolset(String inpath,String outpath,double allratio,int sigratio) {
		if(inpath==null || inpath.length()==0 || outpath==null || outpath.length()==0) return;
		if(allratio>1 || sigratio<1) {
			System.err.println(allratio>1?"allratio 不可大于1！":"sigratio 不可小于1！");
			return;
		}
		try {
			CsvWriter cw=new CsvWriter(new BufferedWriter(new FileWriter(new File(outpath))), ',');
			CsvReader cr=new CsvReader(new FileReader(new File(inpath)));
			while(cr.readRecord()) {
				String line=cr.getRawRecord();
				String [] words=line.split(",");
				if(sigratio != 1) cw.write(words[0]);
				for(int i=sigratio-1;i<words.length*allratio;i+=sigratio) {
					cw.write(words[i]);
				}
				cw.flush();
				cw.endRecord();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算csv文件两行之间的欧几里得距离<P/>
	 * 
	 * |1行1行    1行2行    1行3行    。。。。   1行n行 | <P/>
	 * |2行1行    2行2行    2行3行    。。。。   2行n行 | <P/>
	 * |。。。                                                                            |<P/>
	 * |n行1行    n行2行    n行3行    。。。。    n行n行|<P/>
	 * 
	 * @param path 输入文件路径,不需要后缀名
	 * @param len 输入文件列数
	 */
	public static void caldistEucliden(String path,int dimen,int len) {
		//double [][] distEuclid=new double[97][97];
		double [] arr1=new double [len];
		double [] arr2=new double [len];
		
		String outpath="C:\\Users\\mayi\\Desktop\\可能的数据集\\electricdss-code-r2386-trunk-Distrib-EPRITestCircuits-ckt7\\distEucliden.csv";
		File outfile=new File(outpath);
		
		try {
			CsvWriter cw=new CsvWriter(new BufferedWriter(new FileWriter(outfile)), ',');
			CsvReader cr=new CsvReader(new FileReader(new File(path+".csv")));
			for(int i=0;i<dimen;i++) {
				if(cr.readRecord()) {
					String [] chrs=cr.getRawRecord().split(",");
					for(int k=0;k<len;k++){
						arr1[k]= Double.parseDouble(chrs[k]);
					}
				}
				CsvReader cr2=new CsvReader(new FileReader(new File(path+" - 副本.csv")));
				for(int j=i+1;j<dimen;j++) {
					double sum=0;
					if(cr2.readRecord()) {
						String [] chrs=cr2.getRawRecord().split(",");
						for(int k=0;k<len;k++){
							arr2[k]= Double.parseDouble(chrs[k]);
							sum+=Math.pow(arr1[k]-arr2[k], 2);
						}
					}
					cw.write(((Double)Math.sqrt(sum)).toString());
				}
				cw.flush();
				cw.endRecord();
			}
			cw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("写入成功！");
	}
	
	/**
	 * 递归
	 * @param path 文件或文件夹的绝对地址
	 * @return 这个文件，或者这个文件夹中文件的列表
	 */
	public static List<File> getFiles(String path){
		File root=new File(path);
		List<File> files=new ArrayList<File>();
		if(!root.isDirectory()) {
			files.add(root);
		}
		else {
			for(File f:root.listFiles()) {
				files.addAll(getFiles(f.getAbsolutePath()));
			}
		}
		return files;
	}
	
	/**
	 * 将csv文件分割
	 */
	public static void cutcsv() {
		
	}
	
	/**
	 * 读取多个csv文件，把每个csv文件中我需要的第三列变为行写入一个csv文件中。
	 * 最后得到的csv文件的行数应该与那多个csv文件的个数相同。
	 */
	public static void docsv() {
		
		String outpath="C:\\Users\\mayi\\Desktop\\可能的数据集\\electricdss-code-r2386-trunk-Distrib-EPRITestCircuits-ckt7\\voltageseries.csv";
		
		File outfile=new File(outpath);
		
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter(outfile));
			CsvWriter cwriter=new CsvWriter(bw,',');
			List<File> files=getFiles("C:\\Users\\mayi\\Desktop\\可能的数据集\\electricdss-code-r2386-trunk-Distrib-EPRITestCircuits-ckt7\\电压数据");
			//去重
			
			for(File infile : files) {
				infile.setReadable(true);
				infile.setWritable(true);
				CsvReader creader=new CsvReader(new FileReader(infile));
				//String line="";
				creader.readHeaders();
				while(creader.readRecord()) {
					creader.getRawRecord();
					cwriter.write(creader.get("V1"));
				}
				cwriter.flush();
				cwriter.endRecord();
				creader.close();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 输出exports文件
	 * Export monitor S_1000663-DA1_VI_vs_Time 形式
	 */
	public static void exportexs() {
		String pathname="C:\\Users\\mayi\\Desktop\\可能的数据集\\electricdss-code-r2386-trunk-Distrib-EPRITestCircuits-ckt7\\Services_ckt7 - 副本.dss";
		File filename=new File(pathname);
		File writename=new File("C:\\Users\\mayi\\Desktop\\可能的数据集\\electricdss-code-r2386-trunk-Distrib-EPRITestCircuits-ckt7\\exports.dss");
		
		try {
			InputStreamReader reader=new InputStreamReader(new FileInputStream(filename));
			BufferedReader br=new BufferedReader(reader);
			
			writename.createNewFile();
			BufferedWriter bw=new BufferedWriter(new FileWriter(writename));
			
			String line="";
			while((line=br.readLine()) !=null) {
				
				String[] chs=line.split(" ");
				
				if(chs!=null && chs.length>2) {
					String[] subs=chs[2].split("\\.");
					if(subs.length==2) {
						String tmp=chs[1].split("\\.")[1];
						bw.write("Export monitor "+tmp+"_VI_vs_Time\n");
					}
				}
			}
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 输出Monitor文件
	 */
	public static void exportMonitor() {
		
		String pathname="C:\\Users\\mayi\\Desktop\\可能的数据集\\electricdss-code-r2386-trunk-Distrib-EPRITestCircuits-ckt7\\Services_ckt7 - 副本.dss";
		File filename=new File(pathname);
		File writename=new File("C:\\Users\\mayi\\Desktop\\可能的数据集\\electricdss-code-r2386-trunk-Distrib-EPRITestCircuits-ckt7\\tmp.txt");
		
		try {
			InputStreamReader reader=new InputStreamReader(new FileInputStream(filename));
			BufferedReader br=new BufferedReader(reader);
			
			writename.createNewFile();
			BufferedWriter bw=new BufferedWriter(new FileWriter(writename));
			
			String line="";
			while((line=br.readLine()) !=null) {
				
				String[] chs=line.split(" ");
				
				if(chs!=null && chs.length>2) {
					String[] subs=chs[2].split("\\.");
					if(subs.length==2) {
						String tmp=chs[1].split("\\.")[1];
						bw.write("New monitor."+tmp+"-VI_vs_Time Line."+tmp+" 2 Mode=0\n");
						
					}
				}
			}
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}