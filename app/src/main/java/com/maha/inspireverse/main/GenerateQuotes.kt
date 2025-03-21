package com.maha.inspireverse.main

object GenerateQuotes {
    fun generateMotivationalQuery(): String {
        // Expanded Nature terms
        val natureTerms = listOf(
            "sunrise", "mountains", "ocean waves", "forests", "horizons",
            "blooming flowers", "cascading waterfalls", "misty valleys", "vivid skies", "serene lakes"
        )

        // Expanded Abstract terms
        val abstractTerms = listOf(
            "light rays", "geometric patterns", "vibrant colors", "abstract shapes", "glowing orbs",
            "dynamic lines", "fractal designs", "color gradients", "mesmeric spirals", "celestial patterns"
        )

        // Expanded Action/Motion terms
        val actionTerms = listOf(
            "running", "climbing", "trails", "paths", "footprints",
            "courageous steps", "energetic leaps", "determined strides", "challenging ascents", "pioneering journeys"
        )

        // Randomly select one term from each category and combine them into a single string query
        return "${natureTerms.random()} ${abstractTerms.random()} ${actionTerms.random()}"
    }
    fun generatePopularQuery(): String {
        // List of urban-related popular terms
        val urbanTerms = listOf(
            "cityscape", "skyscrapers", "urban streets", "modern architecture",
            "bridge at night", "city lights", "downtown vibe", "concrete jungle"
        )

        // List of lifestyle-related popular terms
        val lifestyleTerms = listOf(
            "coffee shop", "street style", "casual fashion", "vintage vibe",
            "cozy interiors", "artistic spaces", "leisure", "boutique"
        )

        // List of technology/innovation-related popular terms
        val technologyTerms = listOf(
            "modern technology", "innovative design", "futuristic", "digital art",
            "tech gadgets", "creative workspace", "startup culture", "cyber aesthetic"
        )

        // Randomly select one term from each category
        val urban = urbanTerms.random()
        val lifestyle = lifestyleTerms.random()
        val tech = technologyTerms.random()

        // Combine the selected terms into a single search query string
        return "$urban $lifestyle $tech"
    }

    fun generateSuccessQuery(): String {
        // List capturing achievement impact and innovation aspects
        val achievementTerms = listOf(
            "achievement", "triumph", "breakthrough", "innovation", "legacy", "victory"
        )

        // List capturing the inner mindset required for success
        val mindsetTerms = listOf(
            "ambition", "dedication", "drive", "passion", "focus", "resilience"
        )

        // List capturing visual or symbolic imagery of success
        val visualTerms = listOf(
            "podium", "trophy", "medal", "celebration", "milestone", "peak"
        )

        // Randomly select one term from each list
        val achievement = achievementTerms.random()
        val mindset = mindsetTerms.random()
        val visual = visualTerms.random()

        // Combine them into a single search query string
        return "$achievement $mindset $visual"
    }
    fun generateLoveQuery(): String {
        // List of love expressions or emotional states
        val loveExpressions = listOf(
            "romance", "passion", "affection", "adoration", "devotion", "infatuation"
        )

        // List of emotional descriptors that add nuance
        val emotionalDescriptors = listOf(
            "heartfelt", "tender", "warm", "intimate", "gentle", "fervent"
        )

        // List of symbolic imagery related to love
        val symbolicImagery = listOf(
            "rose", "heart", "embrace", "serenade", "blossom", "cupid"
        )

        // Randomly select one term from each list
        val expression = loveExpressions.random()
        val descriptor = emotionalDescriptors.random()
        val imagery = symbolicImagery.random()

        // Combine them into a single search query string
        return "$expression $descriptor $imagery"
    }

    fun generateRelationshipQuery(): String {
        // List capturing the core essence of relationships
        val relationshipEssence = listOf(
            "connection", "bond", "union", "partnership", "link", "alliance"
        )

        // List capturing qualities and attributes of successful relationships
        val qualityDescriptors = listOf(
            "deep", "genuine", "supportive", "enduring", "trusting", "harmonious"
        )

        // List capturing symbolic imagery often associated with relationships
        val symbolismTerms = listOf(
            "embrace", "intertwined", "handshake", "fusion", "circle", "knot"
        )

        // Randomly select one term from each list
        val essence = relationshipEssence.random()
        val quality = qualityDescriptors.random()
        val symbolism = symbolismTerms.random()

        // Combine the selected terms into a single search query string
        return "$essence $quality $symbolism"
    }
}