package endcrypt.equinoxEquestrian.equine.attributes;

public enum Trait {

    NONE("None", "Neutral", "None Value", 0),

    // A Traits
    ADVENTUROUS("Adventurous", "Positive", "Learns new skills faster", 3000),
    AGGRESSIVE("Aggressive", "Negative", "Horse can be aggressive to rider, other horses, or people. Can kick or buck.", 500),
    AGILE("Agile", "Positive", "Able to switch gaits sooner and gallop longer", 3000),
    ANXIOUS("Anxious", "Negative", "Hates everything", 500),

    // B Traits
    BRAVE("Brave", "Positive", "Rarely spooks, bucks, rears or bolts, scared of nothing", 3000),

    // C Traits
    CALM("Calm", "Positive", "Relaxed and chill", 3000),
    CLUMSY("Clumsy", "Neutral", "Trips up more often, may bump into jumps more", 1000),
    CONFIDENT("Confident", "Positive", "Outgoing, never refuses, confident in what they are doing", 3000),

    // E Traits
    ENERGETIC("Energetic", "Positive", "Faster horses, uses more energy", 3000),
    EXCITABLE("Excitable", "Negative", "May bolt, increases in speed when riding, can become too much", 500),

    // F Traits
    FAITHFUL("Faithful", "Positive", "Faithful towards person they are bonded to", 3000),
    FRIENDLY("Friendly", "Positive", "Less likely to kick a person or horse", 3000),
    FLIGHTY("Flighty", "Negative", "Has a tendency to bolt or rear", 500),
    FLIRTY("Flirty", "Neutral", "Attracted to stallions/mares more, shorter breeding cooldown", 1000),

    // G Traits
    GENTLE("Gentle", "Positive", "Rarely bucks or bolts, always calm", 3000),
    GRUMPY("Grumpy", "Negative", "Likely to buck/kick out", 500),
    GRACIOUS("Gracious", "Positive", "Catches judges' eyes and rarely messes up in disciplines", 3000),
    GREEDY("Greedy", "Negative", "Tends to eat more, cause damage to get to food, etc.", 500),

    // H Traits
    HIGH_STRUNG("High Strung", "Negative", "Tends to be overly excitable or tense", 500),
    HOT("Hot", "Negative", "Bucks, rears, bolts, hard stops", 500),
    HYPER("Hyper", "Positive", "Faster than normal", 3000),
    HONEST("Honest", "Positive", "Rarely bucks, bolts, or rears, rarely refuses a jump", 3000),

    // I Traits
    INTELLIGENT("Intelligent", "Positive", "Learns skills faster than average", 3000),

    // L Traits
    LAZY("Lazy", "Negative", "May be slower than average horse, decreases in speed", 500),
    LUSTFUL("Lustful", "Neutral", "Attracted to mares/stallions, shorter breeding cooldown", 1000),

    // M Traits
    MAJESTIC("Majestic", "Positive", "Elegant appearance", 3000),
    MISCHIEVOUS("Mischievous", "Neutral", "Can be a little tricky, often playful", 1000),

    // N Traits
    NERVOUS("Nervous", "Negative", "May refuse obstacles/jumps, needs more time to train", 500),

    // O Traits
    OBEDIENT("Obedient", "Positive", "Always listens and follows commands", 3000),
    ORNERY("Ornery", "Negative", "Tends to be hard to work with or stubborn", 500),

    // P Traits
    PASSIONATE("Passionate", "Positive", "Intense dedication to work", 3000),
    PLAYFUL("Playful", "Positive", "Less likely to sustain/cause pasture injuries", 3000),
    PROUD("Proud", "Positive", "Shows off and holds a strong, dignified presence", 3000),

    // Q Traits
    QUIET("Quiet", "Neutral", "Doesn't make much noise or cause disturbances", 1000),

    // S Traits
    SHY("Shy", "Negative", "Tends to avoid attention or people", 500),
    SKITTISH("Skittish", "Negative", "Easily startled, prone to bolting", 500),
    SOCIABLE("Sociable", "Positive", "Enjoys interaction with people and other horses", 3000),
    SLUGGISH("Sluggish", "Negative", "Slower than normal", 500),
    STUBBORN("Stubborn", "Negative", "May refuse jumps/obstacles frequently", 500),

    // T Traits
    TALKATIVE("Talkative", "Neutral", "Neighs a lot", 1000),
    TROUBLED("Troubled", "Negative", "Prone to behavioral issues", 500),

    // U Traits
    UNSOCIABLE("Unsociable", "Neutral", "Prefers to be alone", 1000);

    private final String traitName;
    private final String traitType;
    private final String description;
    private final int price;

    Trait(String traitName, String traitType, String description, int price) {
        this.traitName = traitName;
        this.traitType = traitType;
        this.description = description;
        this.price = price;
    }

    public String getTraitName() {
        return traitName;
    }

    public String getTraitType() {
        return traitType;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }
}
