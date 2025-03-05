# wrapper/linguExplorer_original.py
import sys
import os

def load_config():
    # Lade die eingebettete Konfigurationsdatei
    if getattr(sys, "frozen", False):  # Überprüfe, ob die Datei eingefroren (als EXE) ist
        app_dir = sys._MEIPASS  # PyInstaller-erzeugter Temp-Ordner
    else:
        app_dir = os.path.dirname(os.path.abspath(__file__))
    
    config_path = os.path.join(app_dir, "config_temp.properties")
    with open(config_path, "r") as f:
        return f.read().strip()

if __name__ == "__main__":
    config = load_config()
    print(f"Konfiguration: {config}")
