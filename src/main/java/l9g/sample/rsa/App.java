package l9g.sample.rsa;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Dr. Thorsten Ludewig
 */
public class App
{

  /** Zufallszahlengenerator */
  private static final Random RANDOM = new Random(System.currentTimeMillis());

  /** Primzahlenfeld */
  private static int[] primeNumbers;

  //~--- methods --------------------------------------------------------------

  /**
   * Hauptmethode
   *
   *
   * @param args
   *
   *
   * @throws FileNotFoundException
   */
  public static void main(String[] args) throws FileNotFoundException
  {
    System.out.println( "Writing 5 examples to your HOME directory." );
    
    // Den Ausgabekanal der Console (vom Bildschirm)
    // auf die Datei $HOME/rsa-aufgaben.txt umlenken.
    System.setOut(new PrintStream(System.getProperty("user.home")
                                       + File.separator + "rsa-aufgaben.txt"));
    
    // mittels des Sieb des Eratostenes die Primzahlen von 1 bis 100 ermitteln
    // für p und q
    int numberOfPrimeNumbers = findPrimeNumbers(100);

    // p und q dürfen nicht 1 sein deshalb nehmen wir eine Zahl weniger
    numberOfPrimeNumbers--;

    // Wir denken uns 5 Aufgaben aus und schreiben sie heraus
    for (int i = 0; i < 5; i++)
    {
      // zufällige Primzahl für p ermitteln
      int p = randomPrime(numberOfPrimeNumbers);
      int q;

      // zufällige Primzahl für q ermitteln, sie muss aber ungleich p sein
      while ((q = randomPrime(numberOfPrimeNumbers)) == p) {}

      // RSA Beispiel mit p und q berechnen und ausgeben
      rsaExample(p, q);
    }
  }

  /**
   * Method description
   *
   *
   * @param p
   * @param q
   *
   */
  public static void rsaExample(int p, int q)
  {
    System.out.write(12);    // neue Seite für den Import in LibreOffice
    
    // Definitionen und p und q ausgeben
    System.out.println("def. p, q, e are prime numbers");
    System.out.println("def. p != q;  p > 1; q > 1");
    System.out.println("\np=" + p + "  q=" + q);

    // N und pN (Phi N) ermitteln
    int N = p * q;
    int pN = (p - 1) * (q - 1);

    // Rechnung zu N und pN ausgeben
    System.out.println("N=p*q=" + p + "*" + q + "=" + N + "  pN=(p-1)*(q-1)="
                       + (p - 1) + "*" + (q - 1) + "=" + pN);

    // e aus pN ermitteln
    int e = findE(pN);

    // e und public key ausgeben
    System.out.println("e=" + e);
    System.out.println("\npublic key = [" + e + ", " + N + "]");

    // d ermitteln
    int d = findD(e, pN);

    // d und private key ausgeben
    System.out.println("d=" + d);
    System.out.println("\nprivate key = [" + d + ", " + N + "]");
    
    
    // Verschlüsselung testen 
    System.out.println("\nencryption");

    // Zufällige Nachricht M wählen
    int M = RANDOM.nextInt(N);    // M < N

    // M und Bedingung für M ausgeben
    System.out.println("def. 0<M<N  ->  0<" + M + "<" + N);
    System.out.println("M=" + M);

    // Integer Werte M und N in BigInteger Werte umwandeln
    BigInteger x = new BigInteger(Integer.toString(M));
    BigInteger bigN = new BigInteger(Integer.toString(N));

    // Berechnung von C (Crypt) verschlüsselter Wert
    // und Ausgabe von M^e
    x = x.pow(e);
    System.out.println(M + "^" + e + " = " + formatBigInt(x));
    x = x.mod(bigN);

    // BigInt x (Crypt-Wert) in normalen Interger-Wert C konvertieren
    int C = x.intValue();

    // Ausgabe der Rechnung
    System.out.println("C= M^e mod N = " + M + "^" + e + " mod " + N + " = "
                       + C);
    
    // Entschlüsselung testen 
    System.out.println("\ndecryption");
    // Verschlüsselten Wert Ausgeben
    System.out.println("C=" + C);
    
    // Umwandeln von C in BigInteger
    x = new BigInteger(Integer.toString(C));
    // Berechnung und Ausgabe von C^d
    x = x.pow(d);
    System.out.println(C + "^" + d + " = " + formatBigInt(x));
    x = x.mod(bigN);

    // Rückwandlung der Entschlüsselten Nachricht in einen normalen Integer
    int DM = x.intValue();

    // Ausgabe der Rechnung
    System.out.println("M= C^d mod N = " + C + "^" + d + " mod " + N + " = "
                       + DM);

    // wenn die Entschlüsselte Nachricht nicht der Ursprungsnachricht 
    // entspricht ist etwas faul --> Programm abbrechen
    if (DM != M)
    {
      System.exit(1); // ist glücklicherweise nie passiert
    }
  }

  /**
   * Method description
   *
   *
   * @param e
   * @param pN
   *
   * @return
   */
  private static int findD(final int e, final int pN)
  {
    System.out.println("\nfinding d");

    int d;
    int ce = e;
    int cpN = pN;
    ArrayList<DataRow> dataList = new ArrayList<>();
    int R;
    DataRow row, row1;
    int i = 1;

    // Tabellenkopf ausgeben
    System.out.println("   |     e |    pN |     x |     R |     a |     b");
    System.out.println("--------------------------------------------------");

    do
    {
      // neue Zeile mit aktuellem e und pN anlegen
      row = new DataRow(ce, cpN);
      
      // Zeilen x und R berechnen und ausgeben
      row.x = ce / cpN;
      R = row.R = ce % cpN;
      System.out.print(String.format("%2d | ", i) + row);

      // Rechenweg ausgeben
      if (i == 1)
      {
        System.out.print("   e1=" + ce + "; pN1=" + cpN);
      }
      else
      {
        System.out.print("   e" + i + "=pN" + (i - 1) + "; pN" + i + "=R"
                         + (i - 1));
      }

      System.out.println("; x" + i + "=" + "e" + i + "/pN" + i + "; R" + i
                         + "=" + "e" + i + " mod pN" + i);
      
      // Zeile der Liste (Tabelle) hinzufügen
      dataList.add(row);
      
      // neue Werte für e und pN 
      ce = cpN;
      cpN = R;
      i++;
    }
    while (R > 0); // Solange wiederholen bis R == 0

    // Nun zurück rechnen
    // Kopf ausgeben
    System.out.println();
    System.out.println("   |     e |    pN |     x |     R |     a |     b");
    System.out.println("--------------------------------------------------");
    i = dataList.size();
    row = dataList.get(i - 1);
    row.b = 1;
    // Erste Rückzeile ausgeben
    System.out.println(String.format("%2d | ", i) + row + "   b" + i + "=1");

    for (i = dataList.size() - 1; i > 0; i--)
    {
      // a und b Spalten berechen
      row1 = dataList.get(i - 1);
      row1.b = row.a - (row1.x * row.b);
      row1.a = row.b;
      // Zeile ausgeben
      System.out.print(String.format("%2d | ", i) + row1);
      System.out.println("   b" + i + "=a" + (i + 1) + "-(x" + i + "*b"
                         + (i + 1) + "); a" + i + "=b" + (i + 1));
      row = row1;
    }

    d = row.a;

    // wenn d negativ dann von pN abziehen 
    if (d < 0)
    {
      System.out.println("d = " + pN + " + " + d);
      d = pN + d;
    }

    // wenn d > pN dann solange pN von d abziehen bis d < pN
    while (d > pN)
    {
      System.out.println("d = " + d + " - " + pN);
      d = d - pN;
    }

    // Regel und d ausgeben
    System.out.println("def. 0<d<pN  ->  0<" + d + "<" + pN);

    return d; // d zurückgeben
  }

  /**
   * Ermittlung von E
   *
   *
   * @param pN
   *
   * @return
   */
  private static int findE(int pN)
  {
    System.out.println("\nfinding e");
    System.out.println("def. e und pN teilerfremd -> ggT(e,pN) == 1");

    int s = 1;
    int x = pN;

    System.out.print(pN + "=");
    
    // Primzahlenfaktorisierung 
loop:
    while (x > 1) // So lange x > 1
    {
      while (x % primeNumbers[s] == 0) // Während sich x durch die aktuelle 
                                       // Primzahl teilen läßt
      {
        System.out.print(" " + primeNumbers[s]); // Primzahl ausgeben
        x /= primeNumbers[s];                    // x durch Primzahl teilen
        System.out.print("[" + x + "]");         // Aktuellen x-Wert ausgeben

        if (x == 1)       // Wenn x == 1 können wir beide Schleifen beenden.
        {
          break loop;
        }
      }

      s++;
    }

    System.out.println(); // Zeilenende ausgeben

    return primeNumbers[++s]; // Die nächst höhere Primzahl als E verwenden.
  }

  /**
   * Das Sieb des Eratostenes zur Ermittlung von Primzahlen bis maxNumber
   *
   * @param maxNumber
   *
   * @return Anzahl gefundener Primzahlen
   */
  private static int findPrimeNumbers(int maxNumber)
  {
    // das Sieb als Byte-Feld, ist automatisch komplett mit 0 initialisiert.
    // da der Feld-Index bei Null beginnt brauchen wir eine Feld mehr.
    byte[] sieve = new byte[maxNumber + 1];
    
    // s ist start und step / Anfang und Schrittweite beginnend mit 2; 
    int s = 2;
    
    // i ist die Laufvariable, die durch das Sieb (Feld) läuft;
    int i;
    
    // wir brauchen nur bis zum Wurzelwert von maxNumber laufen
    // dann haben wir alle Primzahlen ermittelt.
    int bound = (int) Math.sqrt(maxNumber);

    // los gehts -> solange der Start (s) kleiner bound ist
    while (s < bound)
    {
      // wir überspringen alle NICHT Primzahlen die wir schon gefunden haben. 
      while (sieve[s] == 1)
      {
        s++;
      }

      // Die Laufvariable i beginnt bei 2 * s
      i = s + s;

      // solange i < maxNumber werden alle Einträge im Sieb auf 1 gesetzt.
      // die Schrittweite ist hierbei s
      while (i < maxNumber)
      {
        sieve[i] = 1;
        i += s;
      }

      // s um 1 erhöhen und dann die Schleife wiederholen
      s++;
    }

    // alle gefundenen Primzahlen in eine Liste eintragen
    ArrayList<Integer> p = new ArrayList<>();

    for (i = 1; i < maxNumber; i++)
    {
      if (sieve[i] == 0)
      {
        p.add(i);
      }
    }

    // Aus Performance (Geschwindigkeits)-Gründen übertragen wir
    // die Liste in ein natives Array (Feld)
    primeNumbers = new int[p.size()];
    i = 0;

    for (Integer x : p)
    {
      primeNumbers[i++] = x;
    }

    // Anzhal der gefundenen Primzahlen zurückgeben
    return primeNumbers.length;
  }

  /**
   * Umwandlung von BigInt in einen String mit Ausgabe der Anzahl der Stellen.
   * Ist die Zahl länger als 40 Stellen so werden nur die ersten 37 Stellen
   * und dann "..." ausgegeben.
   *
   * @param bi
   *
   * @return Der formatierte String
   */
  private static String formatBigInt(BigInteger bi)
  {
    String fs = bi.toString();
    int l = fs.length();

    if (l > 40)
    {
      fs = fs.substring(0, 36) + "...";
    }

    fs += " [" + l + "] Stellen";

    return fs;
  }

  /**
   * Zufällige Primzahl ermitteln von 2 bis bound
   *
   * @param bound Grenze im Primzahlenfeld
   *
   * @return Die zufällige Primzahl
   */
  private static int randomPrime(int bound)
  {

    // RANDOM liefrt eine Zufallszahl von 0 bis (bound-1)
    // Um nicht 1 zu erhalten zählen wir einfach 1 dazu und
    // ermitteln dann die Zahl aus dem Primzahlenfeld primeNumbers
    return primeNumbers[RANDOM.nextInt(bound) + 1];
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * DataRow dient zur Speicherung und Ausgabe der Tabellenzeilen
   *
   * @version        1.0
   * @author         daniel
   */
  static class DataRow
  {

    /**
     * Constructs ...
     *
     *
     * @param _e
     * @param _pN
     */
    public DataRow(int _e, int _pN)
    {
      e = _e;
      pN = _pN;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public String toString()
    {
      return String.format("%5d | %5d | %5d | %5d | %5d | %5d", e, pN, x, R, a,
                           b);
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    public int R;

    /** Field description */
    public int a;

    /** Field description */
    public int b;

    /** Field description */
    public int e;

    /** Field description */
    public int pN;

    /** Field description */
    public int x;
  }
}
