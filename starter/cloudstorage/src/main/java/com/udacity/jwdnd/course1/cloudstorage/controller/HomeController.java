package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeServer homeServer;

    @Autowired
    private FileServer fileServer;
    @Autowired
    private NoteServer noteServer;

    @Autowired
    private CredentialServer credentialServer;
    @Autowired
    private UserService userService;

    private static final String ACTIVE_TAB = "activeTab";
    private static final String ERROR = "error";
    private static final String SUCCESS = "success";


    @GetMapping()
    public String getHomePage(Authentication authentication, Model model) {
        User user = userService.getUser(authentication.getName());

        List<File> files = fileServer.findAllByUserId(user.getUserId());
        if (!files.isEmpty()) {
            model.addAttribute(Element.FILES.label, files);
        }

        List<Note> notes = noteServer.findAllByUserId(user.getUserId());
        if (!notes.isEmpty()) {
            model.addAttribute(Element.NOTES.label, notes);
        }

        List<Credential> credentials = credentialServer.findAllByUserId(user.getUserId());
        if (!credentials.isEmpty()) {
            model.addAttribute(Element.CREDENTIALS.label, credentials);
        }

        setDefaultTab(model);

        return "home";
    }

    //FILE
    @PostMapping("")
    public RedirectView uploadFile(Authentication authentication, @ModelAttribute MultipartFile fileUpload, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUser(authentication.getName());
            fileServer.saveFile(fileUpload, user.getUserId());
            setUpSuccessMsg(redirectAttributes, Element.FILES);
        } catch (RuntimeException r) {
            setUpError(redirectAttributes, r.getMessage());
        }

        setUpTab(redirectAttributes, Element.FILES.tab);

        return getRedirectHome();
    }

    @ResponseBody
    @GetMapping("/file-view/{id}")
    public ResponseEntity<ByteArrayResource> viewFile(@PathVariable("id") int id) {
        File file = fileServer.findFileById(id);

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getContenttype()))
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"").body(new ByteArrayResource(file.getFiledata()));
    }

    @GetMapping("/file-delete/{id}")
    public RedirectView deleteFile(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            fileServer.delete(id);
            setUpSuccessMsg(redirectAttributes, Element.FILES);
        } catch (Exception e) {
            setUpError(redirectAttributes, e.getMessage());
        }

        setUpTab(redirectAttributes, Element.FILES.tab);

        return getRedirectHome();
    }

    //NOTE
    @PostMapping("/note")
    public RedirectView createOrEditNote(Authentication authentication, @ModelAttribute Note note, RedirectAttributes redirectAttributes) {
        try {
            //Update or Create
            if (note.getNoteId() != null) {
                noteServer.update(note);
            } else {
                User user = userService.getUser(authentication.getName());

                note.setUserId(user.getUserId());
                noteServer.createNote(note);
            }
            setUpSuccessMsg(redirectAttributes, Element.NOTES);
        } catch (RuntimeException r) {
            setUpError(redirectAttributes, r.getMessage());
        }
        setUpTab(redirectAttributes, Element.NOTES.tab);

        return getRedirectHome();
    }

    @GetMapping("/note-delete/{id}")
    public RedirectView deleteNote(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            noteServer.delete(id);
            setUpSuccessMsg(redirectAttributes, Element.NOTES);
        } catch (Exception e) {
            setUpError(redirectAttributes, e.getMessage());
        }

        setUpTab(redirectAttributes, Element.NOTES.tab);

        return getRedirectHome();
    }

    //CREDENTIALS
    @PostMapping("/credential")
    public RedirectView createOrEditCredential(Authentication authentication, @ModelAttribute Credential credential, RedirectAttributes redirectAttributes) {
        try {
            //Update or Create
            if (credential.getCredentialId() != null) {
                credentialServer.update(credential);
            } else {
                User user = userService.getUser(authentication.getName());

                credential.setUserId(user.getUserId());
                credentialServer.create(credential);
            }
            setUpSuccessMsg(redirectAttributes, Element.CREDENTIALS);
        } catch (RuntimeException r) {
            setUpError(redirectAttributes, r.getMessage());
        }
        setUpTab(redirectAttributes, Element.CREDENTIALS.tab);

        return getRedirectHome();
    }

    @GetMapping("/credential-delete/{id}")
    public RedirectView deleteCredential(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            credentialServer.delete(id);
            setUpSuccessMsg(redirectAttributes, Element.CREDENTIALS);
        } catch (Exception e) {
            setUpError(redirectAttributes, e.getMessage());
        }

        setUpTab(redirectAttributes, Element.CREDENTIALS.tab);

        return getRedirectHome();
    }


    private void setDefaultTab(Model model) {
        if (!model.containsAttribute(ACTIVE_TAB)) {
            model.addAttribute(ACTIVE_TAB, Element.FILES.tab);
        }
    }

    private RedirectView getRedirectHome() {
        return new RedirectView("/home", true);
    }

    private void setUpTab(RedirectAttributes redirectAttributes, String tap) {
        redirectAttributes.addFlashAttribute(ACTIVE_TAB, tap);
    }

    private void setUpError(RedirectAttributes redirectAttributes, String error) {
        redirectAttributes.addFlashAttribute(ERROR, error);
    }

    private void setUpSuccessMsg(RedirectAttributes redirectAttributes, Element element) {
        redirectAttributes.addFlashAttribute(SUCCESS, element.label);
    }

    enum Element {
        FILES("files"), NOTES("notes"), CREDENTIALS("credentials");

        public String tab = "";
        public String label = "";

        Element(String val) {
            this.tab = val;
            this.label = val;
        }
    }
}
