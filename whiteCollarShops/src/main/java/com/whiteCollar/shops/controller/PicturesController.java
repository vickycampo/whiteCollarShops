package com.whiteCollar.shops.controller;

import com.whiteCollar.shops.data.Pictures;
import com.whiteCollar.shops.data.PicturesRepository;

import com.whiteCollar.shops.data.Shops;
import com.whiteCollar.shops.data.ShopsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/shops/{id}/pictures") // This means URL's start with /demo (after Application path)
public class PicturesController
{
    @Autowired // This means to get the bean called shopsRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private PicturesRepository picturesRepository;
    private String SubmitMessage = "";
    private String MessageType = "success";
    @Autowired
    private ShopsRepository shopsRepository;



    /* POST /shops/{id}/pictures - Añadir cuadro*/
    @PostMapping
    public String addNewPainting (Model model,
                                  @PathVariable String id ,
                                  @RequestParam String name ,
                                  @RequestParam String author,
                                  @RequestParam String price,
                                  @RequestParam String _method)
    {
        /* check the method */
        if ( _method.equals("DELETE") )
        {
            /* WE GO TO THE DELETE FUNCTION */
            return burnDown( model , id );
        }
        else if ( _method.equals("post") )
        {
            /* Get old values */
            model.addAttribute("FormName" , name);
            model.addAttribute("FormAuthor" , author);
            model.addAttribute("FormPrice" , price);

            /* Check that the id is a valud number */
            int shopId = 0;
            try
            {
                shopId = Integer.parseInt(id);
            }
            catch (Exception e)
            {
                SubmitMessage = "The id has to be a valid number.";
                MessageType = "error";
                model.addAttribute("messageType", MessageType);
                model.addAttribute("message", SubmitMessage);
                SubmitMessage = "";
                MessageType = "success";
                return pictureHome( model , id);
            }

            /* check that we have a shop with that id */
            try
            {
                /* Get the shops details */
                Shops shop = shopsRepository.findById( shopId );
                String shopName = shop.getName();
                int maxCapacity = shop.getCapacity();
                model.addAttribute("shopName", shopName);
                // get the number of pictures in this shop.
                List<Pictures> picturesList = picturesRepository.findByShopid( shopId );
                if (picturesList.size() < maxCapacity)
                {

                }
                else if (picturesList.size() >= maxCapacity)
                {
                    SubmitMessage = "The shop is full and you cannot add anymore pictrues.";
                    MessageType = "error";
                    model.addAttribute("messageType", MessageType);
                    model.addAttribute("message", SubmitMessage);
                    SubmitMessage = "";
                    MessageType = "success";
                    return pictureHome( model , id);
                }
            }
            catch ( Exception e)
            {
                SubmitMessage = "The id doesn't correspond to any shop. ";
                MessageType = "error";
                model.addAttribute("messageType", MessageType);
                model.addAttribute("message", SubmitMessage);
                SubmitMessage = "";
                MessageType = "success";
                return pictureHome( model , id);
            }

            /* check that the name is not blank */
            if ( name != "")
            {
                /* if the author is left blank we use anonymous */
                if (author == "")
                    author = "Anonymous";

                /* check the price */
                double priceValue;
                try
                {
                    priceValue = Double.parseDouble(price);
                }
                catch ( Exception e)
                {
                    SubmitMessage = "The price is not correct.";
                    MessageType = "error";
                    model.addAttribute("messageType", MessageType);
                    model.addAttribute("message", SubmitMessage);
                    SubmitMessage = "";
                    MessageType = "success";
                    return pictureHome( model , id);
                }

                /* If we get here means everything is ok. */
                /* prepare a date based */
                LocalDate arrivalDate = LocalDate.now();

                Pictures picture = new Pictures();
                picture.setName(name);
                picture.setAuthor(author);
                picture.setPrice(priceValue);
                picture.setArrival_date(arrivalDate.toString());
                picture.setShopid(shopId);
                picturesRepository.save(picture);
                SubmitMessage = "Pictured Saved";
                model.addAttribute("FormName" , "");
                model.addAttribute("FormAuthor" , "");
                model.addAttribute("FormPrice" , "");
                return pictureHome( model , id);
            }
            else
            {
                SubmitMessage = "You must fill the Name field.";
                MessageType = "error";
                model.addAttribute("messageType", MessageType);
                model.addAttribute("message", SubmitMessage);
                SubmitMessage = "";
                MessageType = "success";
                return pictureHome( model , id);
            }
        }
        return pictureHome( model , id);


    }
    /* GET /shops/{ID}/pictures - Listar los cuadro de una tienda */
    @GetMapping
    public String  pictureHome(Model model , @PathVariable String id)
    {
        /* We fill out the previous form */
        if ( ! model.containsAttribute( "FormName")  )
        {
            model.addAttribute("FormName" , "");
            model.addAttribute("FormAuthor" , "");
            model.addAttribute("FormPrice" , "");
        }

        /* Check that the id is a valud number */
        int shopId = 0;
        try
        {
            shopId = Integer.parseInt(id);
        }
        catch (Exception e)
        {
            SubmitMessage = e.getMessage();
            MessageType = "error";
            model.addAttribute("messageType", MessageType);
            model.addAttribute("message", SubmitMessage);
            SubmitMessage = "";
            MessageType = "success";
            return "pictures";
        }
        /* Get the details of the shop*/
        int maxCapacity = 0;
        try
        {
            /* Get the shops details */
            Shops shop = shopsRepository.findById( shopId );
            String shopName = shop.getName();

            maxCapacity = shop.getCapacity();
            model.addAttribute("shopName", shopName);
            model.addAttribute("shopId", shopId);

        }
        catch ( Exception e)
        {
            SubmitMessage = e.getMessage();
            MessageType = "error";
            model.addAttribute("messageType", MessageType);
            model.addAttribute("message", SubmitMessage);
            SubmitMessage = "";
            MessageType = "success";
            return "pictures";
        }

        /* Get all the pictures from that shop */
        List<Pictures> pictures = new ArrayList<>();
        picturesRepository.findByShopid( shopId ).forEach(i -> pictures.add(i));
        model.addAttribute("PicturesList", pictures);

        /* Check if we can add more pictures */
        if ( pictures.size() >= maxCapacity )
        {
            model.addAttribute("EnableForm", false);
        }
        else
        {model.addAttribute("EnableForm", true);
        }

        /* add any available message */
        model.addAttribute("messageType", MessageType);
        model.addAttribute("message", SubmitMessage);
        SubmitMessage = "";
        MessageType = "success";
        return "pictures";
    }
    /* DELETE /shops/{ID}/pictures: Delete all */
    @DeleteMapping (value = "/shops/{ID}/pictures")
    public String burnDown( Model model , @PathVariable String id )
    {
        int shopId = 0;
        try
        {
            shopId = Integer.parseInt(id);
            picturesRepository.findByShopid( shopId ).forEach(i -> picturesRepository.delete( i ));

            SubmitMessage = "Pictures? what pictures?";
            MessageType = "success";
            model.addAttribute("messageType", MessageType);
            model.addAttribute("message", SubmitMessage);
            SubmitMessage = "";
            MessageType = "success";
            return "burnDown";
        }
        catch (Exception e)
        {
            SubmitMessage = "The shop id is not valid. ";
            MessageType = "error";
            return pictureHome( model , id);
        }

    }
}