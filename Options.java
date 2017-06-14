/*
 
 Call-put ratio > 1 —> Stock price decreases
 
 Volume exceeds open interest —> Stock price increases
 
 Large bid-ask spread —> No change in stock
 
 Change increases —> Stock Price Increases
 
 Average of volume increases —> Stock price increases
 */

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kalyani Shirwalkar
 */
public class Options {

    static int flag = 0, k = 3;
    static double minsup, minconf;
    static Map<String, HashSet<Integer>> data = new HashMap<>();
    static TreeMap<String, Integer> fdata = new TreeMap<>();
    static TreeMap<String, Integer> inter = new TreeMap<>();

    public static void main(String args[]) throws FileNotFoundException, IOException {
        if (args[0].equals("-g")) {
            generateColumns();
        }
        if (args[0].equals("-m")) {
            
            minsup = Double.parseDouble(args[1]);
            minconf = Double.parseDouble(args[2]);
            
            getData();

            generateSet();

            for (String key : inter.keySet()) {
                getRules(inter.firstKey());
            }
        } else {
            System.out.println("Incorrect mode");
        }
    }

    public static void generateColumns() throws FileNotFoundException, IOException {
        String line, l[] = null, change, fav, cp, d = "";
        int n = 0;
        float call, put, cpr = 0, c1 = 0, c2 = 0, oi1, oi2, sp = 0, c1avg = 0;
        Scanner sc = new Scanner((new BufferedInputStream(System.in)));
        line = sc.nextLine();
        line = sc.nextLine();
        if (line != null) {
            l = line.split(",");
            d = l[2];
            if (l[9].equals("-")) {
                call = 0;
            } else {
                call = Float.parseFloat(l[9]);
            }
            if (l[16].equals("-")) {
                put = 0;
            } else {
                put = Float.parseFloat(l[16]);
            }
            if (call != 0 && put != 0) {
                cpr += put / call;
            }
            if (l[6].equals("-")) {
                oi1 = 0;
            } else {
                oi1 = Float.parseFloat(l[6]);
            }
            if (l[13].equals("-")) {
                oi2 = 0;
            } else {
                oi2 = Float.parseFloat(l[13]);
            }
            c1 += oi1;
            c2 += oi2;
            sp = Float.parseFloat(l[1]);
            n++;
        }
        while (sc.hasNext()) {
            line = sc.nextLine();
            l = line.split(",");
            if (l[2].equals(d)) {
                if (l[9].equals("-")) {
                    call = 0;
                } else {
                    call = Float.parseFloat(l[9]);
                }
                if (l[16].equals("-")) {
                    put = 0;
                } else {
                    put = Float.parseFloat(l[16]);
                }
                if (call != 0 && put != 0) {
                    cpr += put / call;
                }
                if (l[6].equals("-")) {
                    oi1 = 0;
                } else {
                    oi1 = Float.parseFloat(l[6]);
                }
                if (l[13].equals("-")) {
                    oi2 = 0;
                } else {
                    oi2 = Float.parseFloat(l[13]);
                }
                c1 += oi1;
                c2 += oi2;
                sp = Float.parseFloat(l[1]);
                n++;
            } else {
                cpr = cpr / n;
                n = 1;
                if (cpr > 1) {
                    cp = "CPR > 1";
                } else {
                    cp = "CPR < 1";
                }
                c1 = c1 / n;
                c2 = c2 / n;
                if (c1 > c1avg) {
                    fav = "Call Change Increase";
                } else {
                    fav = "Call Change Decrease";
                }
                c1avg = c1;
                if (sp > Float.parseFloat(l[1])) {
                    change = "Decrease";
                } else if (sp < Float.parseFloat(l[1])) {
                    change = "Increase";
                } else {
                    change = "No Change";
                }
                System.out.println(cp + "," + fav + "," + change + "\n");
                d = l[2];
                if (l[9].equals("-")) {
                    call = 0;
                } else {
                    call = Float.parseFloat(l[9]);
                }
                if (l[16].equals("-")) {
                    put = 0;
                } else {
                    put = Float.parseFloat(l[16]);
                }
                if (call != 0 && put != 0) {
                    cpr += put / call;
                }
                if (l[10].equals("-")) {
                    oi1 = 0;
                } else {
                    oi1 = Float.parseFloat(l[10]);
                }
                if (l[17].equals("-")) {
                    oi2 = 0;
                } else {
                    oi2 = Float.parseFloat(l[17]);
                }
                c1 += oi1;
                c2 += oi2;
                sp = Float.parseFloat(l[1]);
                n++;
            }
        }
        cpr = cpr / n;
        n = 1;
        if (cpr > 1) {
            cp = "CPR > 1";
        } else {
            cp = "CPR < 1";
        }
        c1 = c1 / n;
        c2 = c2 / n;
        if (c1 > c1avg) {
            fav = "Call Change Increase";
        } else {
            fav = "Call Change Decrease";
        }
        c1avg = c1;
        if (sp > Float.parseFloat(l[1])) {
            change = "Stock Price Decrease";
        } else if (sp < Float.parseFloat(l[1])) {
            change = "Stock Price Increase";
        } else {
            change = "No Change in Stock Price";
        }
        if (sp > Float.parseFloat(l[1])) {
            change = "Stock Price Decrease";
        } else if (sp < Float.parseFloat(l[1])) {
            change = "Stock Price Increase";
        } else {
            change = "No Change in Stock Price";
        }
        System.out.println(cp + "," + fav + "," + change + "\n");
    }

    public static void getData() throws FileNotFoundException {

        String line, l[];
        int n = 0;
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            line = sc.nextLine();
            l = line.split(",");
            n++;
            for (int i = 0; i < l.length; i++) {
                if (data.containsKey(l[i])) {
                    HashSet<Integer> transactions = data.get(l[i]);
                    transactions.add(n);
                    data.put(l[i], transactions);
                } else {
                    HashSet<Integer> transactions = new HashSet<>();
                    transactions.add(n);
                    data.put(l[i], transactions);
                }
            }
        }
        for (String key : data.keySet()) {
            n = data.size();
            
            if ((double) data.get(key).size() / n >= minsup) {
                //System.out.println("key = " + key + "size = " + data.get(key).size());
                fdata.put(key, data.get(key).size());
                
            }
        }

    }

    public static void generateSet() {

        Map.Entry p1, p2;

        Iterator it = fdata.entrySet().iterator(), j;

        String p, q;

        Map<String, Integer> a = new TreeMap<>();

        HashSet<Integer> s1;
        HashSet<Integer> s2;

        StringBuilder sb = new StringBuilder();

        while (it.hasNext()) {
            p1 = (Map.Entry) it.next();
            a = fdata.tailMap(p1.getKey().toString());
            j = a.entrySet().iterator();
            p2 = (Map.Entry) j.next();

            while (j.hasNext()) {
                sb = new StringBuilder();
                p2 = (Map.Entry) j.next();
                p = (String) p1.getKey();
                q = (String) p2.getKey();

                s1 = new HashSet<>(data.get(p));
                s2 = new HashSet<>(data.get(q));

                s1.retainAll(s2);

                sb.append(p);
                sb.append(",");
                sb.append(q);
                if (s1.size() >= minsup) {
                    inter.put(sb.toString(), s1.size());
                }
            }

        }
        fdata.putAll(inter);
        generateFrequentSets(inter);

    }

    public static void generateFrequentSets(TreeMap<String, Integer> m) {

        inter = new TreeMap<>();
        Map.Entry p1, p2;

        Iterator it = m.entrySet().iterator(), j;

        String p, q;

        int s = 0;

        Map<String, Integer> a = new TreeMap<>();

        while (it.hasNext()) {
            p1 = (Map.Entry) it.next();
            a = m.tailMap(p1.getKey().toString());
            j = a.entrySet().iterator();
            p2 = (Map.Entry) j.next();
            while (j.hasNext()) {
                p2 = (Map.Entry) j.next();
                p = p1.getKey().toString();
                q = p2.getKey().toString();
                if (checkSubString(p, q)) {
                    p = getSubstring(p, q);
                    s = getIntersection(p);
                    if (s >= minsup) {
                        fdata.put(p, s);
                        inter.put(p, s);
                        flag = 1;
                    }
                }
            }
        }
        if (flag == 0) {
            inter = m;
            return;
        }
        flag = 0;
        k++;
        generateFrequentSets(inter);
    }

    public static int getIntersection(String key) {
        String keys[] = key.split(",");
        HashSet<Integer> s1, s2;
        s1 = new HashSet<>(data.get(keys[0]));
        for (int i = 1; i < keys.length; i++) {
            s2 = new HashSet<>(data.get(keys[i]));
            s1.retainAll(s2);
        }
        return s1.size();
    }

    public static boolean checkSubString(String p, String q) {
        String s1[] = p.split(",");
        String s2[] = q.split(",");
        for (int i = 0; i < k - 2; i++) {
            if (!s1[i].equals(s2[i])) {
                return false;
            }
        }

        return true;
    }

    public static String getSubstring(String p, String q) {
        StringBuilder sb = new StringBuilder();
        String s[] = q.split(",");
        sb.append(p).append(",");

        for (int i = k - 2; i < s.length; i++) {
            sb.append(s[i]).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static void getRules(String s) {
        k = 3;
        String st[] = s.split(","), p, q, t;
        StringBuilder sb = new StringBuilder("");
        int n = st.length, sup1, sup2;
        float conf;

        ArrayList<String> set = new ArrayList<>();
        for (int i = 0; i < (1 << n); i++) {
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    sb.append(st[j] + ",");
                }
            }
            if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length() - 1);
                if (!set.contains(sb.toString())) {
                    set.add(sb.toString());
                }
                sb = new StringBuilder("");
            }
        }
        sb = new StringBuilder("");
        for (int i = 0; i < set.size() - 1; i++) {

            p = set.get(i);
            for (int j = 0; j < set.size() - 1; j++) {
                q = set.get(j);
                if (i != j) {
                    if (!(p.contains(q) || q.contains(p))) {
                        t = sb.append(p).append(",").append(q).toString();
                        sup1 = getIntersection(t);
                        sup2 = fdata.get(p);
                        conf = ((float) sup1 / sup2);
                        sb = new StringBuilder("");
                        if (conf >= minconf) {
                            System.out.println(p + " => " + q + " Confidence = " + conf);
                        }
                    }
                }
            }
        }
    }

}
