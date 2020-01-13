package com.lk.soccer.client.common.telegrams;

import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.Telegram;
import com.lk.engine.common.telegraph.TelegramHandler;
import com.lk.engine.soccer.elements.coach.message.PassInstruction;
import com.lk.soccer.client.common.gui.CoachRender;

public class CoachPassInstructionHandler implements TelegramHandler
{
  private final CoachRender coach;
  
  public CoachPassInstructionHandler(final CoachRender coach)
  {
    this.coach = coach;
  }
  
  @Override
  public Processed handle(final Telegram telegram)
  {
    if (telegram.getMsg() == Message.COACH_PASS_INSTRUCTION)
    {
      final PassInstruction passInstruction = (PassInstruction) telegram.getExtraInfo();
      coach.setPassMark(passInstruction.getPos());
    }
    else
    {
      coach.setPassMark(null);
    }
    
    return Processed.YES;
  }
}
