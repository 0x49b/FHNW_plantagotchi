package fhnw.ws6c.plantagotchi.data.state

class GameDecay {

    /**
     * Decays for LUX, CO2, WATER, FERTILIZER
     * LUX_DECAY = 100 percent / 86_400 seconds --> starts to die after one day without Lux
     * LOVE_DECAY = 100 percent / 604_800 seconds --> starts to die after one week without love
     * FERTILIZER_DECAY = 100 percent / 1_209_600 seconds --> starts to die after two week without fertilizer
     * WATER_DECAY = 100 percent / 259_200 seconds --> starts to die after three days without water
     */
    val lux_decay = 100.0f / 86400.0f
    val love_decay = 100.0f / (86400.0f * 7.0f)
    val fertilizer_decay = 100.0f / (86400.0f * 7.0f * 2.0f)
    val water_decay = 100.0f / (86400.0f * 2.0f)

}