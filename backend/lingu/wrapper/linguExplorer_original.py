import sys
import os

def load_config():
    try:
        # Lade die eingebettete Konfigurationsdatei
        if getattr(sys, "frozen", False):  # Überprüfe, ob die Datei eingefroren (als EXE) ist
            app_dir = sys._MEIPASS  # PyInstaller-erzeugter Temp-Ordner
        else:
            app_dir = os.path.dirname(os.path.abspath(__file__))
        
        config_path = os.path.join(app_dir, "config_temp.properties")
        
        if not os.path.exists(config_path):
            raise FileNotFoundError(f"Config file not found at {config_path}")
        
        with open(config_path, "r") as f:
            return f.read().strip()
    except Exception as e:
        print(f"ERROR: {e}")
        sys.exit(1)  # Beende das Programm mit Fehlercode

if __name__ == "__main__":
    config = load_config()
    print(f"Konfiguration: {config}")