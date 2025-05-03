package endcrypt.equinoxEquestrian.horse.enums;

import java.util.ArrayList;
import java.util.List;

public enum Breed {

    NONE("None", "None", "None", 13, 15), // Adjusted minimum hands

    AEGIDIENBERGER("Aegidienberger", "GAITED", "HORSE", 13, 15),
    AKHAL_TEKE("Akhal-Teke", "EXOTIC", "HORSE", 14, 16),
    AMERICAN_INDIAN_HORSE("American Indian Horse", "STOCK", "HORSE", 13, 16.3),
    AMERICAN_MINIATURE_HORSE("American Miniature Horse", "PONY", "PONY", 9, 9),
    AMERICAN_PAINT_HORSE("American Paint Horse", "STOCK", "HORSE", 14, 16.3),
    AMERICAN_QUARTER_HORSE("American Quarter Horse", "STOCK", "HORSE", 14, 16.3),
    AMERICAN_SADDLEBRED("American Saddlebred", "GAITED", "HORSE", 15, 17.3),
    AMERICAN_SHETLAND("American Shetland", "PONY", "PONY", 9, 11.3),
    AMERICAN_SPORT_PONY("American Sport Pony", "PONY", "PONY", 13.2, 14.2),
    AMERICAN_SUGARBUSH_HARLEQUIN_DRAFT("American Sugarbush Harlequin Draft", "DRAFT", "OVERSIZED",  15.2, 17.3), // Adjusted max hands
    AMERICAN_WARMBLOOD("American Warmblood", "WARMBLOOD", "HORSE", 15, 17.3),
    ANDALUSIAN("Andalusian", "BAROQUE", "HORSE", 15, 16.3),
    ANGLO_ARABIAN("Anglo-Arabian", "HOT-BLOODED", "HORSE", 15.2, 16.3), // Size adjusted to HORSE
    APPALOOSA("Appaloosa", "STOCK", "HORSE", 14, 16.3),
    APPENDIX_HORSE("Appendix Horse", "STOCK", "HORSE", 15, 17.3),
    ARABIAN("Arabian", "EXOTIC", "COB", 14, 15.3),
    ARALOOSA("Araloosa", "STOCK", "HORSE", 14, 15), // Adjusted height
    ARDENNES("Ardennes", "DRAFT", "OVERSIZED", 15.3, 16), // Adjusted height
    AUSTRALIAN_BRUMBY("Australian Brumby", "WILD", "HORSE", 13, 16), // Adjusted max hands
    AUSTRALIAN_RIDING_PONY("Australian Riding Pony", "PONY", "PONY", 12.2, 14.2),
    AUSTRALIAN_STOCK_HORSE("Australian Stock Horse", "STOCK", "HORSE", 14, 16.2),
    AUSTRALIAN_WARMBLOOD("Australian Warmblood", "WARMBLOOD", "HORSE", 15, 17),
    AUSTRIAN_WARMBLOOD("Austrian Warmblood", "WARMBLOOD", "HORSE", 15.2, 16.2),
    AZTECA("Azteca", "BAROQUE", "HORSE", 14.3, 15.1),
    BAJAU_PONY("Bajau Pony", "PONY", "PONY", 12, 14),
    BARB_HORSE("Barb Horse", "BAROQUE", "HORSE", 13.3, 15.3),
    BAROCK_PINTO("Barock Pinto", "BAROQUE", "HORSE", 15, 16.3),
    BAVARIAN_WARMBLOOD("Bavarian Warmblood", "WARMBLOOD", "HORSE", 15.2, 16.2),
    BELGIAN_DRAFT("Belgian Draft", "DRAFT", "OVERSIZED", 16, 17),
    BELGIAN_RIDING_PONY("Belgian Riding Pony", "PONY", "PONY", 10.3, 14),
    BELGIAN_SPORT_HORSE("Belgian Sport Horse", "WARMBLOOD", "HORSE", 16, 17),
    BELGIAN_WARMBLOOD("Belgian Warmblood", "WARMBLOOD", "HORSE", 16, 17),
    BLACK_FOREST_HORSE("Black Forest Horse", "EXOTIC", "HORSE", 14.3, 16),
    BLAZER("Blazer", "STOCK", "HORSE", 13, 15),
    BOULONNAIS("Boulonnais", "DRAFT", "OVERSIZED", 15.2, 16.2),
    BRAZILIAN_SPORT_HORSE("Brazilian Sport Horse", "WARMBLOOD", "HORSE", 16.2, 16.3),
    BRETON("Breton", "DRAFT", "OVERSIZED", 15.2, 16.2),
    BRITISH_RIDING_PONY("British Riding Pony", "PONY", "PONY", 12.2, 14.2),
    BRITISH_SPOTTED_PONY("British Spotted Pony", "PONY", "PONY", 12.2, 14.2),
    BRITISH_WARMBLOOD("British Warmblood", "WARMBLOOD", "HORSE", 16, 17),
    BUDYONNY("Budyonny", "STOCK", "HORSE", 15.2, 16.2),
    CALABRESE("Calabrese", "STOCK", "HORSE", 16, 16.2), // Type adjusted
    CAMARGUE("Camargue", "EXOTIC", "HORSE", 13.2, 14.2),
    CAMPOLINA("Campolina", "GAITED", "HORSE", 15.2, 16.2),
    CANADIAN_SPORT_HORSE("Canadian Sport Horse", "WARMBLOOD", "HORSE", 16.2, 16.3),
    CASPIAN_HORSE("Caspian Horse", "EXOTIC", "PONY", 10, 12.2),
    CHEVAL_CANADIAN("Cheval Canadian", "BAROQUE", "HORSE", 15.2, 16.2),
    CHINCOTEAGUE_PONY("Chincoteague Pony", "PONY", "PONY", 13.2, 14.2),
    CLEVELAND_BAY("Cleveland Bay", "WARMBLOOD", "HORSE", 16, 16.2),
    CLYDESDALE("Clydesdale", "DRAFT", "OVERSIZED", 16.2, 18),
    CONNEMARA("Connemara", "PONY", "PONY", 13, 14.2),
    COLORADO_RANGER("Colorado Ranger", "STOCK", "HORSE", 14.2, 15.2),
    CRIOLLO("Criollo", "GAITED", "HORSE", 14, 15.2),
    CURLY_HORSE("Curly Horse", "EXOTIC", "HORSE", 13.2, 15),
    CZECH_WARMBLOOD("Czech Warmblood", "WARMBLOOD", "HORSE", 16, 16.2),
    DALES_PONY("Dales Pony", "PONY", "PONY", 0, 14.2), // Adjusted min hands
    DANISH_SPORT_PONY("Danish Sport Pony", "PONY", "PONY", 13.2, 14.2),
    DANISH_WARMBLOOD("Danish Warmblood", "WARMBLOOD", "HORSE", 16.2, 17),
    DARTMOOR_PONY("Dartmoor Pony", "PONY", "PONY", 0, 12.2), // Adjusted min hands
    DOLE_HORSE("Dole Horse", "DRAFT", "HORSE", 15.2, 16.2),
    DUTCH_HARNESS_HORSE("Dutch Harness Horse", "DRAFT", "OVERSIZED", 15.2, 16.2), // Type adjusted
    DUTCH_HEAVY_DRAFT("Dutch Heavy Draft", "DRAFT", "OVERSIZED", 16.2, 17),
    DUTCH_RIDING_PONY("Dutch Riding Pony", "PONY", "PONY", 14.2, 15.2),
    DUTCH_WARMBLOOD("Dutch Warmblood", "WARMBLOOD", "HORSE", 16.2, 17),
    EXMOOR_PONY("Exmoor Pony", "WILD", "PONY", 0, 12.3), // Adjusted min hands
    ERISKAY_PONY("Eriskay Pony", "PONY", "PONY", 0, 13.2), // Adjusted min hands
    FALABELLA("Falabella", "EXOTIC", "PONY", 0, 9), // Adjusted min hands
    FAROESE_PONY("Faroese Pony", "PONY", "PONY", 12, 13),
    FELL_PONY("Fell Pony", "PONY", "PONY", 0, 14.2), // Adjusted min hands
    FINNISH_WARMBLOOD("Finnish Warmblood", "WARMBLOOD", "HORSE", 15.2, 16.2),
    FLORIDA_CRACKER_HORSE("Florida Cracker Horse", "GAITED", "HORSE", 13.2, 15.2),
    FREDERIKSBORGER("Frederiksborger", "BAROQUE", "HORSE", 15.2, 16.2),
    FRENCH_SADDLE_PONY("French Saddle Pony", "PONY", "PONY", 15.2, 16.3),
    FRENCH_TROTTER("French Trotter", "GAITED", "HORSE", 15.2, 16.2),
    FRIESIAN("Friesian", "BAROQUE", "HORSE", 15.3, 17),
    FRIESIAN_SPORT_HORSE("Friesian Sport Horse", "BAROQUE", "HORSE", 15.3, 17),
    GALICENO("Galiceno", "STOCK", "PONY", 0, 14), // Adjusted min hands
    GEORGIAN_GRANDE("Georgian Grande", "GAITED", "OVERSIZED", 15.2, 16.2),
    GERMAN_RIDING_PONY("German Riding Pony", "PONY", "PONY", 14.2, 15.2),
    GERMAN_SPORT_HORSE("German Sport Horse", "WARMBLOOD", "HORSE", 16, 17),
    GERMAN_WARMBLOOD("German Warmblood", "WARMBLOOD", "HORSE", 16, 17),
    GRONINGEN_HORSE("Groningen Horse", "DRAFT", "OVERSIZED", 15.2, 16.2),
    GYPSY_VANNER("Gypsy Vanner", "EXOTIC", "COB", 14.2, 16),
    HACKNEY_HORSE("Hackney Horse", "HOT-BLOODED", "HORSE", 14.2, 16),
    HACKNEY_PONY("Hackney Pony", "HOT-BLOODED", "PONY", 0, 14.2), // Adjusted min hands
    HAFLINGER("Haflinger", "GRADE", "HORSE", 13.2, 15),
    HANNOVERIAN("Hanoverian", "WARMBLOOD", "HORSE", 16.2, 17.2),
    HEQU_HORSE("Hequ Horse", "STOCK", "HORSE", 13.2, 14.2),
    HIGHLAND_PONY("Highland Pony", "PONY", "PONY", 13, 14.2),
    HOKKAIDO_HORSE("Hokkaido Horse", "PONY", "PONY", 12.2, 13),
    HOLSTEINER("Holsteiner", "WARMBLOOD", "HORSE", 16.2, 17.2),
    HUCUL_HORSE("Hucul Horse", "GAITED", "PONY", 12.1, 13.1),
    HUNGARIAN_WARMBLOOD("Hungarian Warmblood", "WARMBLOOD", "HORSE", 15.2, 16.2),
    ICELANDIC_HORSE("Icelandic Horse", "GAITED", "COB", 12, 14),
    IOMUD_HORSE("Iomud Horse", "GAITED", "HORSE", 14.2, 15.2),
    IRISH_DRAUGHT_HORSE("Irish Draught Horse", "DRAFT", "OVERSIZED", 15.2, 16.2),
    IRISH_SPORT_HORSE("Irish Sport Horse", "WARMBLOOD", "HORSE", 15.2, 16.2),
    JINZHOU_HORSE("Jinzhou Horse", "STOCK", "HORSE", 13.2, 14.2),
    JUTLAND_HORSE("Jutland Horse", "DRAFT", "HORSE", 15.2, 16.2),
    KABARDA_HORSE("Kabarda Horse", "STOCK", "HORSE",14.2, 15.2),
    KARABAIR_HORSE("Karabair Horse", "STOCK", "HORSE", 14.2, 15.2),
    KARABAKH_HORSE("Karabakh Horse", "EXOTIC", "HORSE", 14.2, 15.2),
    KAZAKH_HORSE("Kazakh Horse", "STOCK", "HORSE", 13.2, 14.2),
    KERRY_BOG_PONY("Kerry Bog Pony", "PONY", "PONY", 12.2, 13.2),
    KIGER_MUSTANG("Kiger Mustang", "WILD", "HORSE", 13.2, 15.2),
    KIRDI_PONY("Kirdi Pony", "PONY", "PONY", 12, 13),
    KISBER_FELVER_HORSE("Kisber Felver Horse", "WARMBLOOD", "OVERSIZED", 15.2, 16.2),
    KISO_HORSE("Kiso Horse", "STOCK", "PONY", 13, 13),
    KLADRUBY_HORSE("Kladruby Horse", "BAROQUE", "HORSE", 15.2, 16.2),
    KNABSTRUPPER_HORSE("Knabstrupper Horse", "WARMBLOOD", "HORSE", 15.2, 16.2),
    KUSHUM_HORSE("Kushum Horse", "STOCK", "HORSE", 15, 15.3),
    KUSTANAI_HORSE("Kustanai Horse", "STOCK", "HORSE", 15, 15.3),
    LATVIAN_HORSE("Latvian Horse", "WARMBLOOD", "HORSE", 15.2, 16.2),
    LITHUANIAN_HEAVY_DRAFT("Lithuanian Heavy Draft", "DRAFT", "OVERSIZED", 16.2, 17.2),
    LIPIZZANER("Lipizzaner", "BAROQUE", "HORSE", 15.2, 16.2),
    LOKAI_HORSE("Lokai Horse", "STOCK", "HORSE", 14, 14.2),
    LOSINO_HORSE("Losino Horse", "WARMBLOOD", "HORSE", 15.2, 16.2),
    LUSITANO("Lusitano", "BAROQUE", "HORSE", 14.2, 16), // Added missing breed
    MALOPOLSKI_HORSE("Malopolski Horse", "WARMBLOOD", "HORSE", 15.2, 16.2),
    MANGALARGA("Mangalarga", "GAITED", "HORSE", 14.2, 15.2),
    MARWARI_HORSE("Marwari Horse", "EXOTIC", "HORSE", 14, 16),
    M_BAYAR_PONY("M'Bayar Pony", "PONY", "PONY", 12.2, 13.2),
    MERENS_PONY("Mérens Pony", "PONY", "PONY", 13.2, 14.2),
    MESSARA_HORSE("Messara Horse", "GAITED", "HORSE", 14, 15.2),
    MISAKI_HORSE("Misaki Horse", "EXOTIC", "PONY", 13.2, 14.2),
    MISSOURI_FOX_TROTTER("Missouri Fox Trotter", "GAITED", "HORSE", 14, 16),
    MIYAKO_PONY("Miyako Pony", "EXOTIC", "PONY", 12.2, 13.2),
    MONGOLIAN_HORSE("Mongolian Horse", "GAITED", "PONY", 12, 14),
    MORAB("Morab", "STOCK", "HORSE", 14.2, 15.2),
    MORGAN("Morgan", "STOCK", "HORSE", 14.1, 15.2),
    MOYLE_HORSE("Moyle Horse", "EXOTIC", "HORSE", 14.2, 15.2),
    MUSTANG("Mustang", "WILD", "HORSE", 13.2, 15),
    MURGESE_HORSE("Murgese Horse", "BAROQUE", "OVERSIZED", 15.2, 16.2),
    NATIONAL_SHOW_HORSE("National Show Horse", "WARMBLOOD", "HORSE", 15.2, 16.2),
    NEW_FOREST_PONY("New Forest Pony", "PONY", "PONY", 12.2, 14.2),
    NEW_KIRGIZ_HORSE("New Kirgiz Horse", "STOCK", "HORSE", 13.2, 14.2),
    NEWFOUNDLAND_PONY("Newfoundland Pony", "PONY", "PONY", 13.2, 14.2),
    NOMA_HORSE("Noma Horse", "PONY", "PONY", 13.2, 14.2),
    NOOITGEDACHT_PONY("Nooitgedacht Pony", "PONY", "PONY", 13.2, 14.2),
    NORIC_HORSE("Noric Horse", "PONY", "PONY", 15.2, 16.2), // Size adjusted
    NORDLAND_HORSE("Nordland Horse", "DRAFT", "HORSE", 14.2, 15.2),
    NORTHEASTERN_CRIOULO("Northeastern Crioulo", "STOCK", "HORSE", 14.2, 15.2),
    NORTH_SWEDISH_HORSE("North Swedish Horse", "DRAFT", "HORSE", 14.2, 15.2),
    NORWEGIAN_FJORD_HORSE("Norwegian Fjord Horse", "EXOTIC", "COB", 13.2, 14.2),
    OB_PONY("Ob Pony", "PONY", "PONY", 12.2, 13.2),
    OLDENBURGER("Oldenburger", "WARMBLOOD", "HORSE", 16.2, 17.2),
    ORLOV_TROTTER("Orlov Trotter", "GAITED", "HORSE", 15.2, 16.2),
    PANTANEIRO_CRIOULO("Pantaneiro Crioulo Horse", "STOCK", "HORSE", 14.2, 15.2),
    PASO_FINO("Paso Fino", "GAITED", "HORSE", 14, 15.2),
    PERCHERON("Percheron", "DRAFT", "HORSE", 16.2, 17.2), // Size adjusted
    PERUVIAN_PASO("Peruvian Paso", "GAITED", "HORSE", 14.2, 15.2),
    PINDOS_PONY("Pindos Pony", "PONY", "PONY", 12.2, 13.2),
    PINIA_HORSE("Pinia Horse", "STOCK", "PONY", 13.2, 14.2),
    PINTABIAN("Pintabian", "STOCK", "HORSE", 15.2, 16.2),
    POLISH_KONIK("Polish Konik", "WILD", "HORSE", 12.2, 13.2),
    PONY_OF_THE_AMERICAS("Pony of the Americas", "PONY", "PONY", 11.2, 14),
    POTTOK("Pottok", "PONY", "PONY", 12.2, 13.2),
    PRZEWALSKI("Przewalski", "WILD", "PONY", 12.2, 14),
    PYRENEAN_TARPAN("Pyrenean Tarpan", "WILD", "PONY", 13.2, 14.2),
    QATGANI_HORSE("Qatgani Horse", "EXOTIC", "HORSE", 13.2, 14.2),
    QUARAB("Quarab", "STOCK", "HORSE", 14.2, 15.2),
    RACKING_HORSE("Racking Horse", "GAITED", "HORSE", 14.2, 16),
    ROCKY_MOUNTAIN_HORSE("Rocky Mountain Horse", "GAITED", "HORSE", 14.2, 16),
    RUSSIAN_DON("Russian Don", "GAITED", "HORSE", 14.2, 15.2),
    RUSSIAN_HEAVY_DRAFT("Russian Heavy Draft", "DRAFT", "OVERSIZED", 15.2, 16.2),
    RUSSIAN_TROTTER("Russian Trotter", "GAITED", "HORSE", 15.2, 16.2),
    SANHE_HORSE("Sanhe Horse", "STOCK", "HORSE", 13.2, 14.2),
    SABLE_ISLAND_HORSE("Sable Island Horse", "WILD", "PONY", 13.2, 14.2),
    SCHLESWIGER_HEAVY_DRAFT("Schleswiger Heavy Draft", "DRAFT", "OVERSIZED", 16.2, 17.2),
    SCHWARZWALDER_FUCHS("Schwarzwälder Fuchs", "DRAFT", "OVERSIZED", 14.2, 15.2),
    SELLE_FRANCAIS("Selle Francais", "WARMBLOOD", "HORSE", 16, 17.2),
    SHAGYA_ARABIAN("Shagya Arabian", "HOT-BLOODED", "HORSE", 15.2, 16.2),
    SHETLAND_PONY("Shetland Pony", "PONY", "PONY", 9.3, 10.2),
    SHIRE("Shire", "DRAFT", "OVERSIZED", 17, 19),
    SINGLE_FOOTING("Single-Footing Horse", "GAITED", "HORSE", 14.2, 16),
    SKYROS_PONY("Skyros Pony", "PONY", "PONY", 9.1, 11),
    SOMALI_PONY("Somali Pony", "PONY", "PONY", 12, 13),
    SORRAIA("Sorraia", "WILD", "HORSE", 13.1, 14.3),
    SOVIET_HEAVY_DRAFT("Soviet Heavy Draft", "DRAFT", "OVERSIZED", 15, 16.2),
    SPANISH_BARB("Spanish Barb", "BAROQUE", "OVERSIZED", 14, 15),
    SPANISH_MUSTANG("Spanish Mustang", "WILD", "HORSE", 13.2, 15),
    SPANISH_NORMAN("Spanish-Norman", "WARMBLOOD", "HORSE", 15.2, 16.2),
    STANDARDBRED("Standardbred", "GAITED", "HORSE", 15, 16.2),
    SUDAN_COUNTRY_BRED("Sudan Country-Bred", "STOCK", "HORSE", 14.2, 15.2),
    SUFFOLK_PUNCH("Suffolk Punch", "DRAFT", "OVERSIZED", 16.1, 17.2),
    SWEDISH_WARMBLOOD("Swedish Warmblood", "WARMBLOOD", "HORSE", 16, 17),
    TAISHUH_HORSE("Taishuh Horse", "STOCK", "HORSE", 12, 13),
    TARPAN("Tarpan", "WILD", "HORSE", 13, 14),
    TAWLEED_HORSE("Tawleed Horse", "EXOTIC", "HORSE", 14.2, 15.2),
    TENNESSEE_WALKING_HORSE("Tennessee Walking Horse", "GAITED", "HORSE", 15, 17),
    TERSK_HORSE("Tersk Horse", "GAITED", "HORSE", 15, 16),
    THESSALIAN_HORSE("Thessalian Horse", "STOCK", "HORSE", 14.2, 15.2),
    THOROUGHBRED("Thoroughbred", "HOT-BLOODED", "HORSE", 15.2, 17),
    TOKARA_PONY("Tokara Pony", "PONY", "PONY", 10, 11),
    TORI_HORSE("Tori Horse", "WARMBLOOD", "COB", 15.2, 16.2),
    TRAKEHNER("Trakehner", "WARMBLOOD", "HORSE", 16, 17),
    UKRAINIAN_SADDLE_HORSE("Ukrainian Saddle Horse", "WARMBLOOD", "HORSE", 15.2, 16.2),
    VLAAMPERD("Vlaamperd", "BAROQUE", "HORSE", 15.1, 16.1),
    VLADIMIR_HEAVY_DRAFT("Vladimir Heavy Draft", "DRAFT", "HORSE", 15.1, 16.1),
    VYATKA_HORSE("Vyatka Horse", "PONY", "PONY", 13.2, 14.2),
    WELARA_HORSE("Welara Horse", "PONY", "PONY", 13.2, 15),
    WELSH_PONY("Welsh Pony", "PONY", "PONY", 11, 13.2),
    WELSH_COB("Welsh Cob", "PONY", "COB", 14.2, 15), // Size adjusted
    WEST_AFRICAN_BARB("West African Barb", "HOT-BLOODED", "COB", 14, 15),
    WESTERN_SUDAN_PONY("Western Sudan Pony", "PONY", "PONY", 13, 14),
    WESTPHALIAN("Westphalian", "WARMBLOOD", "HORSE", 16, 17),
    WIELKOPOLSKI_HORSE("Wielkopolski Horse", "WARMBLOOD", "HORSE", 16, 16.2),
    XILINGOL_HORSE("Xilingol Horse", "WARMBLOOD", "COB", 14, 15),
    YAKUT_HORSE("Yakut Horse", "EXOTIC", "PONY", 13, 14.1),
    YANQI_HORSE("Yanqi Horse", "STOCK", "COB", 14.2, 15.2),
    YILI_HORSE("Yili Horse", "STOCK", "COB", 14, 15),
    YONAGUNI_HORSE("Yonaguni Horse", "PONY", "PONY", 11, 11),
    ZANISKARI_PONY("Zaniskari Pony", "EXOTIC", "PONY", 11, 13),
    ZHEMAICHU_HORSE("Zhemaichu Horse", "PONY", "PONY", 14.2, 15.2);


    private final String name;
    private final String type;
    private final String size;
    private final double minimumHands;
    private final double maximumHands;

    // Constructor
    Breed(String name, String type, String size, double minimumHands, double maximumHands) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.minimumHands = minimumHands;
        this.maximumHands = maximumHands;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public double getMinimumHands() {
        return minimumHands;
    }

    public double getMaximumHands() {
        return maximumHands;
    }

    public static List<String> getBreedNames() {
        List<String> names = new ArrayList<>();
        for (Breed breed : Breed.values()) {
            names.add(breed.getName());
        }
        return names;
    }

    @Override
    public String toString() {
        return name + " (" + type + ", " + size + ")";
    }
}