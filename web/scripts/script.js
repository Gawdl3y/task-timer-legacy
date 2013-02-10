var load, tasks = new Array(), task_count = 0, save_timeout, task_running = new Array(), timer;

$(document).ready(function() {
    // Set some variables
    load = $('#loading');
    
    // Check that they have localStorage and native JSON available
    if(typeof localStorage !== 'object' || typeof JSON !== 'object') {
        $('#out-of-date').show();
        return true;
    }
    
    // Retrieve any tasks they've previously added
    if(localStorage['tasks']) {
        tasks = JSON.parse(localStorage['tasks']);
        task_count = tasks.length;
        
        for(i = 0; i < task_count; i++) {
            list_task(i, 0);
            task_running[i] = false;
        }
    }
    
    // Load settings
    Load();
    
    // Enable the add task fields
    $('.field').removeAttr('disabled');
    
    // Set focus on the new task name field
    setTimeout(function() { $('#new-txt').focus(); }, 100);
    
    // Start the update timer
    update_time();
    
    
    
    // User clicked the Close button in the notice
    $('#close-notice').click(function() {
        $('#notice').fadeOut(600);
        localStorage['hide-notice'] = 'true';
        $('#hide-notice').attr('checked', 'checked');
    });
    
    // User clicked the Add Task button
    $('#new-btn').click(function() {
        var goal = parseFloat($('#new-goal').val());
        
        if($('#new-txt').val() !== '' && goal > 0 && goal <= 80) {
            load.show();
            
            add_task({
                'text': $('#new-txt').val(),
                'current': 0.0,
                'goal': goal
            });
            save();
            
            $('#new-txt').val('');
            load.hide();
        }
    });
    
    // User pressed enter in the task name field
    $('#new-txt').keypress(function (e) {
        if(e.keyCode == 13 && !$('#new-btn').attr('disabled')) {
            $('#new-btn').click();
        }
    });
    
    // User pressed enter in the task goal field
    $('#new-goal').keypress(function (e) {
        if(e.keyCode == 13 && !$('#new-btn').attr('disabled')) {
            $('#new-btn').click();
        }
    });
    
    // User is leaving the page... Save the data.
    $(window).unload(function() {
        save();
    });
    
    // User clicked the Options button
    $('#options').click(function() {
        var height = '225px';
        var width = '500px';
        if(navigator.userAgent.match(/Android|iPod|iPhone|webOS|BlackBerry/)) {
            height = '350px';
            width = '500px';
        }
        
        $('.modal').fadeIn(400, function() { $('#modal-contents').show().animate({'height': height}).animate({'width': width}); });
    });
    
    // User clicked the cancel button in the options modal
    $('#close-modal').click(function() {
        Load();
        
        $('#modal-contents').animate({'width': '0px'}).animate({'height': '0px'}, 400, function() {
            $('.modal').fadeOut(); $('#modal-contents').hide(); 
        });
    });
    
    // User clicked the save button in the options modal
    $('#save-settings').click(function() {
        localStorage['hide-notice'] = $('#hide-notice').is(':checked');
        localStorage['confirm-reset'] = $('#confirm-reset').is(':checked');
        localStorage['confirm-delete'] = $('#confirm-delete').is(':checked');
        
        localStorage['play-sound'] = $('#play-sound').is(':checked');
        localStorage['sound-type'] = $('#sound-type').val();
        localStorage['custom-sound'] = $('#custom-sound').val();
        
        if(parseInt($('#update-time').val()) > 0 && parseInt($('#update-time').val()) < 60) {
            localStorage['update-time'] = parseInt($('#update-time').val());
        }
        
        clearTimeout(timer);
        timer = setTimeout('update_time()', parseInt(localStorage['update-time']) * 1000);
        
        $('#close-modal').click();
    });
    
    // User changed the play sound checkbox
    $('#play-sound').change(function() {
        if($('#play-sound').is(':checked')) {
            $('#sound-type').removeAttr('disabled');
            if($('#sound-type').val() === '2') $('#custom-sound').removeAttr('disabled');
        } else {
            $('#sound-type, #custom-sound').attr('disabled', 'disabled');
        }
    });
    
    // User changed the sound type dropdown
    $('#sound-type').change(function() {
        if($('#sound-type').val() === '2') {
            $('#custom-sound').removeAttr('disabled');
        } else {
            $('#custom-sound').attr('disabled', 'disabled');
        }
    });
    
    save();
    $('#tasks').show();
});



// Load the settings
function Load() {
    // Set default settings if they don't exist
    if(typeof localStorage['hide-notice'] == 'undefined') localStorage['hide-notice'] = 'false';
    if(typeof localStorage['confirm-reset'] == 'undefined') localStorage['confirm-reset'] = 'true';
    if(typeof localStorage['confirm-delete'] == 'undefined') localStorage['confirm-delete'] = 'true';
    if(typeof localStorage['play-sound'] == 'undefined') localStorage['play-sound'] = 'true';
    if(typeof localStorage['sound-type'] == 'undefined') localStorage['sound-type'] = '1';
    if(typeof localStorage['custom-sound'] == 'undefined') localStorage['custom-sound'] = '';
    if(typeof localStorage['update-time'] == 'undefined') localStorage['update-time'] = '1';
    
    $('#sound-type').val(parseInt(localStorage['sound-type']));
    $('#custom-sound').val(localStorage['custom-sound']);
    $('#update-time').val(localStorage['update-time']);
    
    if(localStorage['hide-notice'] === 'true') {
        $('#hide-notice').attr('checked', 'checked');
        $('#notice').hide();
    } else {
        $('#hide-notice').removeAttr('checked');
        $('#notice').show();
    }
    
    if(localStorage['confirm-reset'] === 'true') {
        $('#confirm-reset').attr('checked', 'checked');
    } else {
        $('#confirm-reset').removeAttr('checked');
    }
    
    if(localStorage['confirm-delete'] === 'true') {
        $('#confirm-delete').attr('checked', 'checked');
    } else {
        $('#confirm-delete').removeAttr('checked');
    }
    
    if(localStorage['play-sound'] === 'true') {
        $('#play-sound').attr('checked', 'checked');
        $('#sound-type').removeAttr('disabled');
        if($('#sound-type').val() === '2') {
            $('#custom-sound').removeAttr('disabled');
        } else {
            $('#custom-sound').attr('disabled', 'disabled');
        }
    } else {
        $('#play-sound').removeAttr('checked');
        $('#sound-type').attr('disabled', 'disabled');
        $('#custom-sound').attr('disabled', 'disabled');
    }
    
    // If the user has chosen to use a custom sound, set the audo element's src to the custom sound path
    if(localStorage['sound-type'] === '2') {
        $('#sound').attr('src', localStorage['custom-sound']);
    } else {
        $('#sound').attr('src', 'Deneb.ogg');
    }
}

// Save the data in localStorage
function save(timeout) {
    if(timeout) load.show();
    localStorage['tasks'] = JSON.stringify(tasks);
    
    clearTimeout(save_timeout);
    save_timeout = setTimeout('save(true)', 60000);
    
    window.status = 'Saved.';
    if(timeout) load.hide();
}

// Add a task
function add_task(data) {
    tasks[task_count] = data;
    task_running[task_count] = false;
    list_task(task_count, (task_count === 0 ? 1 : 2));
    
    task_count++;
}

// Reset a task
function reset_task(task) {
    if(localStorage['confirm-reset'] === 'false' || confirm('Are you sure you want to reset task "'+ tasks[task].text +'"?')) {
        tasks[task].current = 0;
        $('#task-'+ task +' td.current').text(format_time(tasks[task].current));
        $('#task-'+ task +' progress').val(progress).text(progress.toString() +'%');
    }
}

// Delete a task
function delete_task(task) {
    if(localStorage['confirm-delete'] === 'false' || confirm('Are you sure you want to delete task "'+ tasks[task].text +'"?')) {
        load.show();
        $('#new-btn').attr('disabled', 'disabled');
        $('#task-'+ task +' button').attr('disabled', 'disabled');
        
        tasks.splice(task, 1);
        task_count--;
        
        if(task_running[task]) toggle_task(task);
        task_running.splice(task, 1);
        
        save();
        
        // Animate accordingly.
        if(task_count === 0) {
            $('#task-list').fadeOut(400, function() {
                $('#task-list tbody').empty();
                $('#no-tasks').fadeIn();
                
                $('#new-btn').removeAttr('disabled');
            });
        } else {
            $('#task-'+ task).fadeOut(400, function() {
                // Rebuild the task list
                $('#task-list tbody').empty();
                for(i = 0; i < task_count; i++) {
                    list_task(i, 0);
                }
                
                $('#new-btn').removeAttr('disabled');
            });
        }
        
        load.hide();
    }
}

// Toggle whether a task is running or not
function toggle_task(task) {
    if(task_running[task]) {
        task_running[task] = false;
        $('#task-'+ task +' button.toggle').text('Start');
        $('#task-'+ task).removeAttr('class');
    } else {
        task_running[task] = true;
        $('#task-'+ task +' button.toggle').text('Stop');
        $('#task-'+ task).attr('class', 'running');
    }
}

// Add the task to the list
function list_task(task, anim) {
    // Progress done
    var progress = Math.floor(tasks[task].current / tasks[task].goal * 100);
    
    // Create the row
    $('#row-template').clone().attr('id', 'task-'+ task).appendTo('#task-list tbody');
    
    // Text
    $('#task-'+ task +' td.text').text(tasks[task].text);
    $('#task-'+ task +' td.current').text(format_time(tasks[task].current));
    $('#task-'+ task +' td.goal').text(tasks[task].goal.toString() + (tasks[task].goal > 1 ? ' Hours' : ' Hour'));
    $('#task-'+ task +' button.toggle').text(task_running[task] ? 'Stop' : 'Start');
    $('#task-'+ task +' progress').val(progress).text(progress.toString() +'%');
    if(task_running[task]) $('#task-'+ task).attr('class', 'running');
    
    // Disable the toggle button if task is at its goal, and change the bg colour
    if(tasks[task].current >= tasks[task].goal - 0.00015) {
        $('#task-'+ task +' button.toggle').attr('disabled', 'disabled');
        $('#task-'+ task).attr('class', 'done');
    }
    
    // Option Buttons
    $('#task-'+ task +' button.toggle').attr('name', task).click(function() {
        toggle_task(parseInt(this.name));
    });
    $('#task-'+ task +' button.delete').attr('name', task).click(function() {
        delete_task(parseInt(this.name));
    });
    
    // Animation
    if(anim === 0) {
        // Show instantly
        $('#no-tasks').hide();
        $('#task-list').show();
        $('#task-'+ task).show();
    } else if(anim === 1) {
        // Fade all at once
        $('#task-'+ task).show();
        $('#no-tasks').fadeOut(400, function() {
            $('#task-list').fadeIn();
        });
    } else {
        // Fade in
        $('#task-'+ task).fadeIn();
    }
}

// Increase the current time on tasks that are running by a second
function update_time() {
    for(i = 0; i < task_count; i++) {
        if(task_running[i]) {
            // Increment time
            tasks[i].current += (1 / 3600) * parseInt(localStorage['update-time']);
            
            // Stop updating this one if it's at the goal, and play the notification sound
            if(tasks[i].current >= tasks[i].goal - 0.00015) {
                tasks[i].current = tasks[i].goal;
                toggle_task(i);
                $('#task-'+ i +' button.toggle').attr('disabled', 'disabled');
                $('#task-'+ i).attr('class', 'done');
                
                if(localStorage['play-sound'] === 'true') document.getElementById('sound').play();
            }
            
            var progress = Math.floor(tasks[i].current / tasks[i].goal * 100);
            
            // Update list
            $('#task-'+ i +' td.current').text(format_time(tasks[i].current));
            $('#task-'+ i +' progress').val(progress).text(progress.toString() +'%')
        }
    }
    
    // Do it again in a second
    timer = setTimeout('update_time()', parseInt(localStorage['update-time']) * 1000);
}

// Format the time to the format hh:mm:ss from a decimal
function format_time(decimal) {
    var hours = Math.floor(decimal);
    var minutes = Math.floor((decimal - hours) * 60);
    var seconds = Math.floor((decimal - hours - (minutes / 60)) * 3600)
    
    minutes = (minutes < 10 ? '0' : '') + minutes.toString();
    seconds = (seconds < 10 ? '0' : '') + seconds.toString();
    
    return hours.toString() +':'+ minutes +':'+ seconds;
}
