using System;
using System.IO;
using System.Diagnostics;
using System.Reflection;

class Program
{
    // Benutzer-ID fest einbetten
    private const int USER_ID = 987654321; // Diese Zeile wird vom Python-Skript ersetzt

    static void Main(string[] args)
    {
        try
        {
            // Extrahiere die eingebettete EXE-Datei
            byte[] exeData = ExtractEmbeddedData("WrapperProject.linguExplorer.exe");

            // Speichere die extrahierte EXE-Datei im temporären Verzeichnis
            string exePath = Path.GetTempFileName() + ".exe";
            File.WriteAllBytes(exePath, exeData);

            // Starte die extrahierte EXE-Datei mit der festen Benutzer-ID als Parameter
            StartExtractedExe(exePath, USER_ID);
        }
        catch (Exception ex)
        {
            // Logging in eine Datei und Konsolenausgabe für Debugging
            string errorMessage = $"Fehler: {ex.Message}";
            Console.WriteLine(errorMessage);

            string logPath = Path.Combine(Path.GetTempPath(), "wrapper_log.txt");
            File.WriteAllText(logPath, errorMessage);
            Console.WriteLine($"Fehlerdetails wurden in {logPath} gespeichert.");
        }

        // Halte das Konsolenfenster offen
        Console.WriteLine("Drücken Sie eine beliebige Taste, um das Programm zu beenden...");
        Console.ReadKey();
    }

    static byte[] ExtractEmbeddedData(string resourceName)
    {
        // Lies die eingebettete Ressource
        var assembly = Assembly.GetExecutingAssembly();
        using (var stream = assembly.GetManifestResourceStream(resourceName))
        {
            if (stream == null) throw new FileNotFoundException("Resource not found: " + resourceName);
            byte[] data = new byte[stream.Length];
            stream.Read(data, 0, data.Length);
            return data;
        }
    }

    static void StartExtractedExe(string exePath, int userid)
    {
        // Starte die extrahierte EXE-Datei mit der Benutzer-ID als Parameter
        ProcessStartInfo startInfo = new ProcessStartInfo
        {
            FileName = exePath,
            Arguments = userid.ToString(), // Benutzer-ID als Argument
            UseShellExecute = false, // Konsolenfenster anzeigen
            CreateNoWindow = true  // Kein verstecktes Fenster
        };

        Console.WriteLine($"Starte {exePath} mit Benutzer-ID: {userid}");
        Process.Start(startInfo);
    }
}