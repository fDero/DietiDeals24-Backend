package utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import entity.Activity;
import entity.Auction;
import response.UserPublicActivity;

public class UserPublicActivitySerializer extends JsonSerializer<UserPublicActivity> {

    @Override
    public void serialize(
        UserPublicActivity value, 
        JsonGenerator gen, 
        SerializerProvider serializers
    )
        throws IOException 
    {
        System.out.println("UserPublicActivitySerializer");
        System.out.println("Activity size: " + value.getActivity().size());
        gen.writeStartArray();
        for (Activity activity : value.getActivity()) {
            System.out.println("Activity: " + activity);
            Auction auction = new Auction(activity);
            gen.writeStartObject();
            AuctionSerializerHelper.serializeBasics(gen, auction);
            AuctionSerializerHelper.serializeBidsData(gen, auction);
            AuctionSerializerHelper.serializeJustOnePictureUrl(gen, auction);
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}